package a311.college.service.impl;

import a311.college.constant.redis.SchoolRedisKey;
import a311.college.dto.school.AddSchoolCommentDTO;
import a311.college.dto.school.SchoolDTO;
import a311.college.dto.school.SchoolPageQueryDTO;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.mapper.college.CollegeMapper;
import a311.college.mapper.resource.ResourceMapper;
import a311.college.result.PageResult;
import a311.college.service.CollegeService;
import a311.college.vo.MajorSimpleVO;
import a311.college.vo.SchoolSimpleVO;
import a311.college.vo.SchoolVO;
import a311.college.vo.YearScoreVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
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

    private final ResourceMapper resourceMapper;

    @Resource
    private RedisTemplate<String, SchoolSimpleVO> redisTemplate;

    @Autowired
    public CollegeServiceImpl(CollegeMapper collegeMapper, ResourceMapper resourceMapper) {
        this.collegeMapper = collegeMapper;
        this.resourceMapper = resourceMapper;
    }

    /**
     * 大学数据分页查询
     *
     * @param collegePageQueryDTO 大学分页查询DTO
     * @return PageResult<SchoolVO>
     */
    @Override
    public PageResult<SchoolSimpleVO> pageSelect(SchoolPageQueryDTO collegePageQueryDTO) {
        String key = SchoolRedisKey.COLLEGE_CACHE_KEY + collegePageQueryDTO.getProvince() + ":";
        List<SchoolSimpleVO> range = redisTemplate.opsForList().range(key, 0, -1);
        if (range != null && !range.isEmpty()) {
            log.info("缓存命中");
            return new PageResult<>((long) range.size(), range);
        }
        log.info("缓存未命中，开启分页查询");
        PageHelper.startPage(collegePageQueryDTO.getPage(), collegePageQueryDTO.getPageSize());
        Page<SchoolSimpleVO> pageResult = collegeMapper.pageQuery(collegePageQueryDTO);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取总记录
        List<SchoolSimpleVO> result = pageResult.getResult();
        // 将其添加到缓存
        redisTemplate.opsForList().rightPushAll(key, result);
        redisTemplate.expire(key, SchoolRedisKey.COLLEGE_CACHE_TTL, TimeUnit.SECONDS);
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
            String key = SchoolRedisKey.COLLEGE_CACHE_KEY + area + ":";
            try {
                // 1. 查询数据库
                List<SchoolSimpleVO> collegeVOS = collegeMapper.selectByAddress(area);
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
     * @return List<SchoolSimpleVO
     */
    @Override
    public List<SchoolSimpleVO> getCollegeByName(String schoolName) {
        return collegeMapper.selectByName(schoolName);
    }

    /**
     * 根据成绩匹配大学
     *
     * @return List<SchoolSimpleVO>
     */
    @Override
    public List<SchoolSimpleVO> getByGrade(UserGradeQueryDTO gradeDTO) {
        List<SchoolSimpleVO> collegeSimpleVOS = collegeMapper.selectByGrade(gradeDTO);
        return new ArrayList<>(collegeSimpleVOS);
    }

    /**
     * 查询大学历年分数线
     *
     * @return List<YearScoreVO>
     */
    @Override
    public List<YearScoreVO> getScoreByYear(YearScoreQueryDTO yearScoreDTO) {
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
    public void addComment(AddSchoolCommentDTO addCommentDTO) {
        collegeMapper.addComment(addCommentDTO);
    }

    /**
     * 优化大学数据
     */
    @Override
    public void addScore() {
        List<SchoolSimpleVO> list = collegeMapper.getAllCollege();
        for (SchoolSimpleVO collegeSimpleVO : list) {
            String rankList = collegeSimpleVO.getRankList();
            String[] split = rankList.split(",");
            int score = getScore(collegeSimpleVO, split);
            collegeSimpleVO.setScore(score);
            collegeMapper.updateScore(collegeSimpleVO);
        }
    }

    /**
     * 获取院校具体信息
     *
     * @param collegeDTO 大学查询DTO
     * @return SchoolVO
     */
    @Override
    public SchoolVO getCollege(SchoolDTO collegeDTO) {
        // 1.获取简略大学信息
        SchoolSimpleVO schoolSimpleVO = collegeMapper.selectBySchoolId(collegeDTO.getSchoolId());
        // 2.封装大学详细信息
        SchoolVO collegeVO = new SchoolVO();
        BeanUtil.copyProperties(schoolSimpleVO, collegeVO);
        // 3.返回随机校园风光
        // 3.1获取所有照片
//        List<String> imageList = resourceMapper.getAllImages();
//        // 3.2从所有照片中随机选取6张
//        List<String> images = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            int index = RandomUtil.randomInt(0, 250);
//            images.add(imageList.get(index));
//        }
//        // 3.3返回随机6张校园风光
//        collegeVO.setImages(images);
        // 4.随机校园配置
        if (schoolSimpleVO.getScore() > 60) {
            // 4.1该学校属于好学校
            collegeVO.setEquipment(highScoreSchool());
        } else if (schoolSimpleVO.getRankList().contains("民办")) {
            // 4.2该学校属于有钱的学校
            collegeVO.setEquipment(richSchool());
        } else {
            // 4.3该学校属于一般学校
            collegeVO.setEquipment(commonSchool());
        }
        // 5.为该学校封装展示专业
        // 5.1获取专业
        List<MajorSimpleVO> collegeSimpleList = collegeMapper.selectSimpleMajor(schoolSimpleVO.getSchoolId());
        // 5.2调整专业格式
        for (MajorSimpleVO collegeSimpleMajorVO : collegeSimpleList) {
            collegeSimpleMajorVO.setMajorName(collegeSimpleMajorVO.getMajorName().split("\n")[0]);
        }
        // 5.3封装
        collegeVO.setMajors(collegeSimpleList);
        return collegeVO;
    }


    private Map<String, Integer> highScoreSchool() {
        Map<String, Integer> equipment = new HashMap<>();
        equipment.put("one", 4);
        equipment.put("two", 1);
        equipment.put("three", RandomUtil.randomInt(5, 16));
        equipment.put("four", 1);
        equipment.put("five", 1);
        equipment.put("six", 1);
        equipment.put("seven", 1);
        equipment.put("eight", 1);
        equipment.put("nine", 1);
        equipment.put("ten", RandomUtil.randomInt(5, 11));
        return equipment;
    }

    private Map<String, Integer> richSchool() {
        Map<String, Integer> equipment = new HashMap<>();
        equipment.put("one", 4);
        equipment.put("two", 1);
        equipment.put("three", RandomUtil.randomInt(3, 7));
        equipment.put("four", 1);
        equipment.put("five", 1);
        equipment.put("six", 1);
        equipment.put("seven", 1);
        equipment.put("eight", 1);
        equipment.put("nine", 1);
        equipment.put("ten", RandomUtil.randomInt(2, 5));
        return equipment;
    }

    private Map<String, Integer> commonSchool() {
        Map<String, Integer> equipment = new HashMap<>();
        Integer[] room = {4, 6};
        equipment.put("one", RandomUtil.randomEle(room));
        Integer[] flag = {0, 1};
        equipment.put("two", RandomUtil.randomEle(flag));
        equipment.put("three", RandomUtil.randomInt(5, 11));
        equipment.put("four", 1);
        equipment.put("five", 1);
        equipment.put("six", RandomUtil.randomEle(flag));
        equipment.put("seven", 1);
        equipment.put("eight", 0);
        equipment.put("nine", RandomUtil.randomEle(flag));
        equipment.put("ten", RandomUtil.randomInt(3, 7));
        return equipment;
    }

    private int getScore(SchoolSimpleVO collegeSimpleVO, String[] split) {
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
        List<SchoolSimpleVO> list = collegeMapper.getAllCollege();
        for (SchoolSimpleVO collegeSimpleVO : list) {
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
