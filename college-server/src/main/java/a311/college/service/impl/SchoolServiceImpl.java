package a311.college.service.impl;

import a311.college.constant.redis.SchoolRedisKey;
import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.SchoolInfo;
import a311.college.entity.user.User;
import a311.college.mapper.resource.ResourceMapper;
import a311.college.mapper.school.SchoolMapper;
import a311.college.mapper.user.UserMapper;
import a311.college.result.PageResult;
import a311.college.service.SchoolService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.major.MajorSimpleVO;
import a311.college.vo.school.*;
import a311.college.vo.user.UserVO;
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
public class SchoolServiceImpl implements SchoolService {

    private final SchoolMapper schoolMapper;

    private final UserMapper userMapper;

    private final ResourceMapper resourceMapper;

    @Resource
    private RedisTemplate<String, SchoolSimpleVO> redisTemplate;

    @Autowired
    public SchoolServiceImpl(SchoolMapper schoolMapper, ResourceMapper resourceMapper, UserMapper userMapper) {
        this.schoolMapper = schoolMapper;
        this.resourceMapper = resourceMapper;
        this.userMapper = userMapper;
    }

    /**
     * 大学信息分页查询
     *
     * @param schoolPageQueryDTO 大学分页查询DTO
     * @return PageResult<SchoolVO>
     */
    @Override
    public PageResult<SchoolSimpleVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO) {
        String key = SchoolRedisKey.SCHOOL_CACHE_KEY + schoolPageQueryDTO.getProvince() + ":";
        List<SchoolSimpleVO> range = redisTemplate.opsForList().range(key, 0, -1);
        if (range != null && !range.isEmpty()) {
            log.info("缓存命中");
            return new PageResult<>((long) range.size(), range);
        }
        log.info("缓存未命中，开启分页查询");
        PageHelper.startPage(schoolPageQueryDTO.getPage(), schoolPageQueryDTO.getPageSize());
        Page<SchoolSimpleVO> pageResult = schoolMapper.pageQuery(schoolPageQueryDTO);
        // 获取总记录数
        long total = pageResult.getTotal();
        // 获取总记录
        List<SchoolSimpleVO> result = pageResult.getResult();
        // 将其添加到缓存
        redisTemplate.opsForList().rightPushAll(key, result);
        redisTemplate.expire(key, SchoolRedisKey.SCHOOL_CACHE_TTL, TimeUnit.SECONDS);
        return new PageResult<>(total, result);
    }

    /**
     * 缓存预热
     * 添加热点地区的大学到缓存
     */
    public void cacheSchool() {
        // 定义热点地区列表
        List<String> hotAreas = Arrays.asList("北京", "上海", "广东", "湖北", "重庆", "陕西", "湖北");
        for (String area : hotAreas) {
            // 统一键名格式
            String key = SchoolRedisKey.SCHOOL_CACHE_KEY + area + ":";
            try {
                // 1. 查询数据库
                List<SchoolSimpleVO> schoolSimpleVOS = schoolMapper.selectByAddress(area);
                // 2. 删除旧缓存（避免残留旧数据）
                redisTemplate.delete(key);
                // 3. 批量插入新数据（使用rightPushAll）
                if (!schoolSimpleVOS.isEmpty()) {
                    redisTemplate.opsForList().rightPushAll(key, schoolSimpleVOS);
                    log.info("地区 {} 缓存预热成功，共 {} 条数据", area, schoolSimpleVOS.size());
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
     * @param schoolName 学校名
     * @return List<SchoolSimpleVO>
     */
    @Override
    public List<SchoolSimpleVO> getSchoolByName(String schoolName) {
        return schoolMapper.selectByName(schoolName);
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradeDTO 用户成绩DTO
     * @return List<SchoolSimpleVO>
     */
    @Override
    public List<SchoolSimpleVO> getSchoolByGrade(UserGradeQueryDTO gradeDTO) {
        List<SchoolSimpleVO> schoolSimpleVOS = schoolMapper.selectByGrade(gradeDTO);
        return new ArrayList<>(schoolSimpleVOS);
    }

    /**
     * 查询大学具体信息
     *
     * @param schoolDTO 大学查询DTO
     * @return SchoolVO 大学具体信息
     */
    @Override
    public SchoolVO getDetailSchool(SchoolDTO schoolDTO) {
        // 1.获取简略大学信息
        SchoolSimpleVO schoolSimpleVO = schoolMapper.selectBySchoolId(schoolDTO.getSchoolId());
        // 2.封装大学详细信息
        SchoolVO schoolVO = new SchoolVO();
        BeanUtil.copyProperties(schoolSimpleVO, schoolVO);
        // 3.返回随机校园风光
        // 3.1获取所有照片
        List<String> imageList = resourceMapper.getAllImages();
        // 3.2从所有照片中随机选取6张
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int index = RandomUtil.randomInt(0, 250);
            images.add(imageList.get(index));
        }
        // 3.3返回随机6张校园风光
        schoolVO.setImages(images);
        // 4.随机校园配置
        if (schoolSimpleVO.getScore() > 60) {
            // 4.1该学校属于好学校
            schoolVO.setEquipment(highScoreSchool());
        } else if (schoolSimpleVO.getRankList().contains("民办")) {
            // 4.2该学校属于有钱的学校
            schoolVO.setEquipment(richSchool());
        } else {
            // 4.3该学校属于一般学校
            schoolVO.setEquipment(commonSchool());
        }
        // 5.为该学校封装展示专业
        // 5.1获取专业
        List<MajorSimpleVO> simpleVOS = schoolMapper.selectSimpleMajor(schoolSimpleVO.getSchoolId());
        // 5.2调整专业格式
        for (MajorSimpleVO simpleVO : simpleVOS) {
            simpleVO.setMajorName(simpleVO.getMajorName().split("\n")[0]);
        }
        // 5.3封装
        schoolVO.setMajors(simpleVOS);
        return schoolVO;
    }

    /**
     * 获取某一院校的历年分数线
     *
     * @param yearScoreDTO 分数线查询DTO
     * @return YearScoreVO
     */
    @Override
    public List<YearScoreVO> scoreLineByYear(YearScoreQueryDTO yearScoreDTO) {
        return schoolMapper.selectScoreByYear(yearScoreDTO);
    }

    /**
     * 用户评价大学
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Override
    public void addSchoolComment(AddSchoolCommentDTO addCommentDTO) {
        schoolMapper.addComment(addCommentDTO);
    }

    /**
     * 录取预测
     *
     * @param forecastDTO 录取预测DTO
     * @return ForecastVO 录取预测VO
     */
    @Override
    public ForecastVO forecast(ForecastDTO forecastDTO) {
        // 0.三个不同难度的专业
        int minimum = 0;
        int stable = 0;
        int rush = 0;
        // 1.处理专业信息
        // 1.1获得专业信息
        List<SchoolMajorVO> schoolMajorVOList = schoolMapper.getAllMajor(forecastDTO);
        // 1.2处理专业名、特殊要求、选科要求
        for (SchoolMajorVO schoolMajorVO : schoolMajorVOList) {
            // 2.1默认根据用户的位次进行统计
            Integer userRanking = forecastDTO.getRanking();
            if (userRanking != null) {
                Integer majorRanking = schoolMajorVO.getMinRanking();
                if (majorRanking >= userRanking - 3000 && majorRanking <= userRanking + 4000) {
                    // 2.2该专业为稳
                    schoolMajorVO.setCategory(1);
                    stable++;
                } else if (majorRanking < userRanking - 3000 && majorRanking >= userRanking - 5000) {
                    schoolMajorVO.setCategory(2);
                    rush++;   // 冲：专业位次更优（数值更小）
                } else if (majorRanking > userRanking + 4000) {
                    schoolMajorVO.setCategory(0);
                    minimum++; // 保：专业位次更差（数值更大）
                }
            } else {
                Integer majorScore = schoolMajorVO.getMinScore();
                Integer userGrade = forecastDTO.getGrade();
                if (majorScore <= userGrade + 10 && majorScore >= userGrade - 10) {
                    schoolMajorVO.setCategory(1);
                    stable++;
                } else if (majorScore > userGrade + 10 && majorScore <= userGrade + 20) {
                    schoolMajorVO.setCategory(2);
                    rush++;
                } else if (majorScore < userGrade - 10 && majorScore >= userGrade - 30) {
                    schoolMajorVO.setCategory(0);
                    minimum++;
                }
            }
        }
        List<SchoolMajorVO> forecastList = new ArrayList<>();
        for (SchoolMajorVO schoolMajorVO : schoolMajorVOList) {
            if (schoolMajorVO.getCategory() != null) {
                forecastList.add(schoolMajorVO);
            }
        }
        ForecastVO forecastVO = new ForecastVO();
        forecastVO.setMajorForecastResultList(forecastList);
        double chance = (stable + minimum + 0.2 * rush) / schoolMajorVOList.size();
        forecastVO.setChance((int) Math.round(chance * 100));
        forecastVO.setSelectableMajor(schoolMajorVOList.size());
        return forecastVO;
    }

    @Override
    public List<BriefSchoolInfoVO> getHotSchool() {
        UserVO user = userMapper.selectById(ThreadLocalUtil.getCurrentId());
        List<BriefSchoolInfoVO> briefSchoolInfoVOList = new ArrayList<>();
        if (user != null) {
            List<SchoolInfo> schoolInfoList = schoolMapper.selectByProvince(user.getProvince());
            for (SchoolInfo schoolInfo : schoolInfoList) {
                schoolInfo.setRankList(schoolInfo.getRankList().split(",")[1]);
                BriefSchoolInfoVO briefSchoolInfoVO = new BriefSchoolInfoVO();
                briefSchoolInfoVO.setName(schoolInfo.getSchoolName());
                briefSchoolInfoVO.setHead(schoolInfo.getSchoolHead());
                briefSchoolInfoVO.setRank(schoolInfo.getRankList());
                briefSchoolInfoVOList.add(briefSchoolInfoVO);
            }
            return briefSchoolInfoVOList;
        } else {
            // 用户没有登录，展示默认的大学
            List<String> hotSchool = new ArrayList<>();
            Collections.addAll(hotSchool, "清华大学", "浙江大学", "四川大学", "中国科学技术大学", "中山大学", "哈尔滨工业大学",
                    "武汉大学", "厦门大学", "西安交通大学", "西南大学");
            for (String school : hotSchool) {
                SchoolInfo schoolInfo = schoolMapper.getByName(school);
                BriefSchoolInfoVO briefSchoolInfoVO = new BriefSchoolInfoVO();
                briefSchoolInfoVO.setName(schoolInfo.getSchoolName());
                briefSchoolInfoVO.setHead(schoolInfo.getSchoolHead());
                briefSchoolInfoVO.setRank(schoolInfo.getRankList().split(",")[1]);
                briefSchoolInfoVOList.add(briefSchoolInfoVO);
            }
            return briefSchoolInfoVOList;
        }
    }

    @Override
    public void updateData() {
        List<SchoolSimpleVO> allSchool = schoolMapper.getAllSchool();
        for (SchoolSimpleVO schoolSimpleVO : allSchool) {
            int rankScore = 0;
            for (String rank : schoolSimpleVO.getRankList().split(",")) {
                if (schoolSimpleVO.getSchoolName().equals("北京大学")) {
                    System.out.printf("\n");
                }
                switch (rank) {
                    case "本科", "双一流", "强基计划" -> rankScore += 15;
                    case "公办", "军事类" -> rankScore += 10;
                    case "985" -> rankScore += 30;
                    case "211" -> rankScore += 20;
                    case "医药类" -> rankScore += 5;
                    case "双高计划" -> rankScore += 3;
                }
            }
            if (schoolSimpleVO.getSchoolName().contains("大学") && !schoolSimpleVO.getRankList().contains("民办")) {
                rankScore += 5;
            }
            schoolSimpleVO.setScore((7 * rankScore + 3 * schoolSimpleVO.getSchoolProvince().getScore()) / 10);
            schoolMapper.updateScore(schoolSimpleVO);
        }
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
}
