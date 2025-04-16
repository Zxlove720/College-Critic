package a311.college.service.impl;

import a311.college.dto.user.UserVolunteerPageDTO;
import a311.college.mapper.volunteer.VolunteerMapper;
import a311.college.result.PageResult;
import a311.college.service.VolunteerService;
import a311.college.vo.volunteer.SchoolVolunteer;
import a311.college.vo.volunteer.ScoreLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
     * @param userVolunteerPageDTO 用户志愿分页查询DTO
     * @return PageResult<SchoolVolunteer>
     */
    @Override
    public PageResult<SchoolVolunteer> showVolunteer(UserVolunteerPageDTO userVolunteerPageDTO) {
        List<SchoolVolunteer> schoolVolunteerList = volunteerMapper.selectVolunteerSchool(userVolunteerPageDTO);
        // 在内存中处理分类逻辑
        schoolVolunteerList.forEach(school ->
                school.getVolunteerList().forEach(volunteer -> {
                    Integer minRanking = volunteer.getScoreLineList().get(0).getMinRanking();
                    volunteer.setCategory(calculateCategory(minRanking, userVolunteerPageDTO.getRanking()));
                    List<ScoreLine> scoreLineList = volunteer.getScoreLineList();
                    for (ScoreLine scoreLine : scoreLineList) {
                        scoreLine.setScoreThanMe(userVolunteerPageDTO.getGrade() - scoreLine.getMinScore());
                        scoreLine.setRankingThanMe(userVolunteerPageDTO.getRanking() - scoreLine.getMinRanking());
                    }
                })
        );
        return manualPage(schoolVolunteerList, userVolunteerPageDTO.getPage(), userVolunteerPageDTO.getPageSize());
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
}
