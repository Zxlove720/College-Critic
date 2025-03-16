package a311.college.service.impl;

import a311.college.dto.college.AddCommentDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.mapper.college.CollegeMapper;
import a311.college.redis.RedisKeyConstant;
import a311.college.result.PageResult;
import a311.college.service.CollegeService;
import a311.college.vo.CollegeSimpleVO;
import a311.college.vo.YearScoreVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 大学相关服务实现类
 */
@Slf4j
@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeMapper collegeMapper;

    @Resource
    private RedisTemplate<String, CollegeSimpleVO> redisTemplate;

    @Autowired
    public CollegeServiceImpl(CollegeMapper collegeMapper) {
        this.collegeMapper = collegeMapper;
    }

    /**
     * 大学数据分页查询
     *
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return PageResult<CollegeVO>
     */
    @Override
    public PageResult<CollegeSimpleVO> pageSelect(CollegePageQueryDTO collegePageQueryDTO) {
        String key = RedisKeyConstant.COLLEGE_CACHE_KEY + collegePageQueryDTO.getProvince() + ":";
        List<CollegeSimpleVO> range = redisTemplate.opsForList().range(key, 0, -1);
        if (range != null && !range.isEmpty()) {
            log.info("缓存命中");
            return new PageResult<>((long) range.size(), range);
        }
        log.info("缓存未命中，开启分页查询");
        PageHelper.startPage(collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        Page<CollegeSimpleVO> pageResult = collegeMapper.pageQuery(collegePageQueryDTO);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取总记录
        List<CollegeSimpleVO> result = pageResult.getResult();
        // 将其添加到缓存
        redisTemplate.opsForList().rightPushAll(key, result);
        redisTemplate.expire(key, RedisKeyConstant.COLLEGE_CACHE_TTL, TimeUnit.SECONDS);
        return new PageResult<>(total, result);
    }

    /**
     * 缓存预热
     * 添加热点地区的大学到缓存
     */
    public void cacheCollege() {
        // 定义热点地区列表
        List<String> hotAreas = Arrays.asList("北京", "上海", "广东", "湖北", "重庆", "陕西");
        for (String area : hotAreas) {
            // 统一键名格式
            String key = RedisKeyConstant.COLLEGE_CACHE_KEY + area + ":";
            try {
                // 1. 查询数据库
                List<CollegeSimpleVO> collegeVOS = collegeMapper.selectByAddress(area);
                // 2. 删除旧缓存（避免残留旧数据）
                redisTemplate.delete(key);
                // 3. 批量插入新数据（使用rightPushAll）
                if (!collegeVOS.isEmpty()) {
                    redisTemplate.opsForList().rightPushAll(key, collegeVOS);
                    log.info("地区 {} 缓存预热成功，共 {} 条数据", area, collegeVOS.size());
                } else {
                    log.warn("地区 {} 无数据，跳过缓存预热", area);
                }
            } catch (Exception e) {
                log.error("地区 {} 缓存预热失败: {}", area, e.getMessage(), e);
            }
        }
    }

    /**
     * 根据学校名搜索大学
     *
     * @return List<CollegeSimpleVO
     */
    @Override
    public List<CollegeSimpleVO> getCollegeByName(String schoolName) {
        return collegeMapper.selectByName(schoolName);
    }

    /**
     * 根据成绩匹配大学
     *
     * @return List<CollegeSimpleVO>
     */
    @Override
    public List<CollegeSimpleVO> getByGrade(GradeDTO gradeDTO) {
        List<CollegeSimpleVO> collegeSimpleVOS = collegeMapper.selectByGrade(gradeDTO);
        return new ArrayList<>(collegeSimpleVOS);
    }

    /**
     * 查询大学历年分数线
     *
     * @return List<YearScoreVO>
     */
    @Override
    public List<YearScoreVO> getScoreByYear(YearScoreDTO yearScoreDTO) {
        List<YearScoreVO> yearScoreVOList = collegeMapper.selectScoreByYear(yearScoreDTO);
        for (YearScoreVO yearScoreVO : yearScoreVOList) {
            yearScoreVO.setMajorName(yearScoreVO.getMajorName()
                    .substring(yearScoreVO.getMajorName().indexOf("选科要求")));
        }
        return yearScoreVOList;
    }

    /**
     * 用户评价大学
     *
     * @param addCommentDTO 评价DTO
     */
    @Override
    public void addComment(AddCommentDTO addCommentDTO) {
        collegeMapper.addComment(addCommentDTO);
    }

    /**
     * 优化大学数据
     *
     */
    @Override
    public void addScore() {
        List<CollegeSimpleVO>list = collegeMapper.getAllCollege();
        for (CollegeSimpleVO collegeSimpleVO : list) {
            String rankList = collegeSimpleVO.getRankList();
            String[] split = rankList.split(",");
            int score = getScore(collegeSimpleVO, split);
            collegeSimpleVO.setScore(score);
            collegeMapper.updateScore(collegeSimpleVO);
        }
    }

    private int getScore(CollegeSimpleVO collegeSimpleVO, String[] split) {
        int score = 0;
        for (String s : split) {
             switch (s) {
                 case "本科", "双一流", "强基计划" -> score += 15;
                 case "公办", "军事类" -> score += 10;
                 case "985" -> score += 30;
                 case "211" -> score += 20;
                 case "医药类" -> score += 5;
                 case "双高计划" -> score += 3;
             }
        }
        if (collegeSimpleVO.getSchoolName().contains("大学")) {
            score += 5;
        }
        return score;
    }

    private void updateRankList() {
        List<CollegeSimpleVO>list = collegeMapper.getAllCollege();
        for (CollegeSimpleVO collegeSimpleVO : list) {
            String rankList = collegeSimpleVO.getRankList();
            String tempResult = rankList.replace("[", "");
            String tempResult2 = tempResult.replace("]", "");
            String result = tempResult2.replaceAll("，", ",");
            result = result.replaceAll(" ", "");
            collegeSimpleVO.setRankList(result);
            collegeMapper.updateRank(collegeSimpleVO);
        }
    }

}
