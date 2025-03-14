package a311.college.service.impl;

import a311.college.dto.college.AddCommentDTO;
import a311.college.dto.college.CollegePageQueryDTO;
import a311.college.dto.query.school.GradeDTO;
import a311.college.dto.query.school.YearScoreDTO;
import a311.college.mapper.college.CollegeMapper;
import a311.college.redis.RedisKeyConstant;
import a311.college.result.PageResult;
import a311.college.result.Result;
import a311.college.service.CollegeService;
import a311.college.thread.ThreadLocalUtil;
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
        List<String> hotAreas = Arrays.asList("北京", "上海", "广东", "湖北", "重庆");

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

    @Override
    public List<CollegeSimpleVO> getByGrade(GradeDTO gradeDTO) {
        List<CollegeSimpleVO> collegeSimpleVOS = collegeMapper.selectByGrade(gradeDTO);
        Set<CollegeSimpleVO> tempSet = new HashSet<>(collegeSimpleVOS);
        return new ArrayList<>(tempSet);
    }

    @Override
    public Result<List<YearScoreVO>> getScoreByYear(YearScoreDTO yearScoreDTO) {
        List<YearScoreVO> yearScoreVOList = collegeMapper.selectScoreByYear(yearScoreDTO);
        for (YearScoreVO yearScoreVO : yearScoreVOList) {
            yearScoreVO.setMajorName(yearScoreVO.getMajorName()
                    .substring(yearScoreVO.getMajorName().indexOf("选科要求")));
        }
        return Result.success(yearScoreVOList);
    }

    @Override
    public Result<List<CollegeSimpleVO>> getCollegeByName(String schoolName) {
        return Result.success(collegeMapper.selectByName(schoolName));
    }

    @Override
    public void addComment(AddCommentDTO addCommentDTO) {
        collegeMapper.addComment(addCommentDTO);
    }

}
