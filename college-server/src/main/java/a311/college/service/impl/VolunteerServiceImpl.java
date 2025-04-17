package a311.college.service.impl;

import a311.college.dto.user.VolunteerPageDTO;
import a311.college.dto.volunteer.AddVolunteerDTO;
import a311.college.entity.volunteer.Volunteer;
import a311.college.exception.ReAdditionException;
import a311.college.exception.volunteer.VolunteerException;
import a311.college.mapper.volunteer.VolunteerMapper;
import a311.college.result.PageResult;
import a311.college.service.VolunteerService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.volunteer.SchoolVolunteer;
import a311.college.vo.volunteer.ScoreLine;
import a311.college.vo.volunteer.VolunteerVO;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerMapper volunteerMapper;

    @Autowired
    public VolunteerServiceImpl(VolunteerMapper volunteerMapper) {
        this.volunteerMapper = volunteerMapper;
    }

    /**
     * 志愿展示
     *
     * @param volunteerPageDTO 用户志愿分页查询DTO
     * @return PageResult<SchoolVolunteer>
     */
    @Override
    public PageResult<SchoolVolunteer> showVolunteer(VolunteerPageDTO volunteerPageDTO) {
        List<SchoolVolunteer> schoolVolunteerList = volunteerMapper.selectVolunteerSchool(volunteerPageDTO);
        // 在内存中处理分类逻辑
        schoolVolunteerList.forEach(school ->
                school.getVolunteerVOList().forEach(volunteerVO -> {
                    Integer minRanking = volunteerVO.getScoreLineList().get(0).getMinRanking();
                    volunteerVO.setCategory(calculateCategory(minRanking, volunteerPageDTO.getRanking()));
                    List<ScoreLine> scoreLineList = volunteerVO.getScoreLineList();
                    for (ScoreLine scoreLine : scoreLineList) {
                        scoreLine.setScoreThanMe(volunteerPageDTO.getGrade() - scoreLine.getMinScore());
                        scoreLine.setRankingThanMe(volunteerPageDTO.getRanking() - scoreLine.getMinRanking());
                    }
                })
        );
        return manualPage(schoolVolunteerList, volunteerPageDTO.getPage(), volunteerPageDTO.getPageSize());
    }

    /**
     * 人工分页
     *
     * @param filterCache 过滤后的学校缓存
     * @param page        查询页码
     * @param pageSize    每页大小
     * @return PageResult<SchoolVO>
     */
    private PageResult<SchoolVolunteer> manualPage(List<SchoolVolunteer> filterCache, Integer page, Integer pageSize) {
        // 1.获取记录总数
        int total = filterCache.size();
        // 2.获取起始页码
        int start = (page - 1) * pageSize;
        if (start >= total) return new PageResult<>((long) total, Collections.emptyList());
        // 3.获取结束页码
        int end = Math.min(start + pageSize, total);
        // 4.分页并返回
        List<SchoolVolunteer> pageData = filterCache.subList(start, end);
        for (SchoolVolunteer schoolVolunteer : pageData) {
            for (VolunteerVO volunteerVO : schoolVolunteer.getVolunteerVOList()) {
                volunteerVO.setIsAdd(volunteerMapper.checkVolunteer(volunteerVO.getMajorId(), ThreadLocalUtil.getCurrentId()) != null);
            }

        }
        return new PageResult<>((long) total, pageData);
    }

    /**
     * 计算专业分类逻辑
     *
     * @param minRanking 专业历年最低分
     * @param ranking  用户分数
     * @return 0保底 1稳妥 2冲刺
     */
    private int calculateCategory(int minRanking, int ranking) {
        if (minRanking >= ranking - 3000 && minRanking <= ranking + 5000) {
            // 该专业为稳
            return 1;
        } else if (minRanking < ranking - 3000 && minRanking >= ranking - 7500) {
            // 该专业为冲
            return 2;
        } else if (minRanking > ranking + 5000) {
            // 该专业为保
            return 0;
        }
        return -1;
    }





    /**
     * 添加志愿
     *
     * @param addVolunteerDTO 添加志愿
     */
    @Override
    public void addVolunteer(AddVolunteerDTO addVolunteerDTO) {
        // 1.判断当前志愿表是否已被填满
        Integer count = volunteerMapper.getCount(addVolunteerDTO.getTableId());
        // 1.1此时说明该志愿表为空，将其设置为0
        if (count == null) {
            count = 0;
        }
        if (count > 96) {
            // 1.2此时该志愿表已经填满，直接返回
            log.error("该志愿表已满");
            throw new VolunteerException("该志愿表已满，请选择其他志愿表");
        }
        // 2.准备添加志愿到志愿表中
        Integer majorId = addVolunteerDTO.getMajorId();
        Long userId = ThreadLocalUtil.getCurrentId();
        // 2.1判断该志愿是否已经被添加到志愿表中
        if (volunteerMapper.checkVolunteer(majorId, userId) != null) {
            // 2.2该志愿已经被添加过了，不允许添加
            throw new ReAdditionException("重复添加");
        }
        // 3.封装Volunteer实体类
        Volunteer volunteer = volunteerMapper.selectSchoolMajorById(majorId);
        BeanUtil.copyProperties(addVolunteerDTO, volunteer);
        volunteer.setUserId(userId);
        volunteer.setCount(count + 1);
        // 4.添加volunteer
        volunteerMapper.addVolunteer(volunteer);
    }

}
