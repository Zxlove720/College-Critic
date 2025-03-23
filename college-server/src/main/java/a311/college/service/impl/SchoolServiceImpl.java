package a311.college.service.impl;

import a311.college.constant.redis.SchoolRedisKey;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.school.*;
import a311.college.dto.query.school.UserGradeQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
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
    private RedisTemplate<String, School> redisTemplate;

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
     * @return PageResult<DetailedSchoolVO>
     */
    @Override
    //TODO该方法的缓存部分有大问题，缓存的数据不支持分页
    public PageResult<School> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO) {
        String key = SchoolRedisKey.SCHOOL_CACHE_KEY + schoolPageQueryDTO.getProvince() + ":";
        List<School> range = redisTemplate.opsForList().range(key, 0, -1);
        if (range != null && !range.isEmpty()) {
            log.info("缓存命中");
            return new PageResult<>((long) range.size(), range);
        }
        log.info("缓存未命中，开启分页查询");
        try (Page<School> page = PageHelper.startPage(schoolPageQueryDTO.getPage(), schoolPageQueryDTO.getPageSize())) {
            schoolMapper.pageQuery(schoolPageQueryDTO);
            // 获取总记录数
            long total = page.getTotal();
            // 获取总记录
            List<School> result = page.getResult();
            if (!result.isEmpty()) {
                // 将其添加到缓存
                redisTemplate.opsForList().rightPushAll(key, result);
                redisTemplate.expire(key, SchoolRedisKey.SCHOOL_CACHE_TTL, TimeUnit.SECONDS);
            }
            return new PageResult<>(total, result);
        } catch (Exception e) {
            log.error("大学信息分页查询失败，报错为：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 大学信息缓存预热
     * 添加热点地区的大学到缓存
     */
    public void cacheSchool() {
        // 定义热点地区列表
        List<String> hotAreas = Arrays.asList("北京", "上海", "广东", "重庆", "天津", "浙江", "江苏", "陕西", "四川", "湖北");
        for (String area : hotAreas) {
            // 统一键名格式
            String key = SchoolRedisKey.SCHOOL_CACHE_KEY + area + ":";
            try {
                // 1. 查询数据库
                List<School> school = schoolMapper.selectByAddress(area);
                // 2. 删除旧缓存（避免残留旧数据）
                redisTemplate.delete(key);
                // 3. 批量插入新数据（使用rightPushAll）
                if (!school.isEmpty()) {
                    redisTemplate.opsForList().rightPushAll(key, school);
                    log.info("地区 {} 缓存预热成功，共 {} 条数据", area, school.size());
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
     * @param schoolNameQueryDTO@return List<SchoolVO>
     */
    @Override
    public List<School> searchSchool(SchoolNameQueryDTO schoolNameQueryDTO) {
        // 1.根据大学名模糊查询大学
        return schoolMapper.selectByName(schoolNameQueryDTO.getSchoolName());
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradeDTO 用户成绩DTO
     * @return List<School>
     */
    @Override
    public List<School> getSchoolByGrade(UserGradeQueryDTO gradeDTO) {
        return schoolMapper.selectByGrade(gradeDTO);
    }

    /**
     * 查询大学具体信息
     *
     * @param schoolDTO 大学查询DTO
     * @return DetailedSchoolVO 大学具体信息
     */
    @Override
    public DetailedSchoolVO getDetailSchool(SchoolDTO schoolDTO) {
        // 1.获取大学信息
        School school = schoolMapper.selectBySchoolId(schoolDTO.getSchoolId());
        // 2.封装大学详细信息
        DetailedSchoolVO detailedSchoolVO = new DetailedSchoolVO();
        BeanUtil.copyProperties(school, detailedSchoolVO);
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
        detailedSchoolVO.setImages(images);
        // 4.随机校园配置
        if (school.getScore() > 60) {
            // 4.1该学校属于好学校
            detailedSchoolVO.setEquipment(highScoreSchool());
        } else if (school.getRankList().contains("民办")) {
            // 4.2该学校属于有钱的学校
            detailedSchoolVO.setEquipment(richSchool());
        } else {
            // 4.3该学校属于一般学校
            detailedSchoolVO.setEquipment(commonSchool());
        }
        // 5.为该学校封装展示专业
        // 5.1获取专业
        List<MajorSimpleVO> majorSimpleVOList = schoolMapper.selectSimpleMajor(school.getSchoolId());
        // 5.2调整专业格式
        for (MajorSimpleVO majorSimpleVO : majorSimpleVOList) {
            majorSimpleVO.setMajorName(majorSimpleVO.getMajorName());
        }
        // 5.3封装
        detailedSchoolVO.setMajors(majorSimpleVOList);
        return detailedSchoolVO;
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
        // 1.列出三种难度的专业
        int minimum = 0;
        int stable = 0;
        int rush = 0;
        // 2.处理专业信息
        // 2.1获得专业信息
        List<SchoolMajor> schoolMajorList = schoolMapper.getAllMajor(forecastDTO);
        // 2.2将SchoolMajor对象封装为SchoolMajorVO对象返回
        List<SchoolMajorVO> schoolMajorVOList = new ArrayList<>();
        for (SchoolMajor schoolMajor : schoolMajorList) {
            // 2.3构造SchoolMajorVo对象
            SchoolMajorVO schoolMajorVO = new SchoolMajorVO();
            // 2.4属性拷贝
            BeanUtil.copyProperties(schoolMajor, schoolMajorVO);
            // 2.5默认根据用户的位次进行预测
            Integer userRanking = forecastDTO.getRanking();
            if (userRanking != null) {
                int majorRanking = schoolMajor.getMinRanking();
                if (majorRanking >= userRanking - 3000 && majorRanking <= userRanking + 4000) {
                    // 2.6该专业为稳
                    schoolMajorVO.setCategory(1);
                    stable++;
                } else if (majorRanking < userRanking - 3000 && majorRanking >= userRanking - 5000) {
                    // 2.7该专业为冲
                    schoolMajorVO.setCategory(2);
                    rush++;
                } else if (majorRanking > userRanking + 4000) {
                    // 2.8该专业为保
                    schoolMajorVO.setCategory(0);
                    minimum++;
                }
            } else if (forecastDTO.getGrade() != null) {
                int majorScore = schoolMajor.getMinScore();
                int userGrade = forecastDTO.getGrade();
                if (majorScore <= userGrade + 10 && majorScore >= userGrade - 10) {
                    // 该专业为稳
                    schoolMajorVO.setCategory(1);
                    stable++;
                } else if (majorScore > userGrade + 10 && majorScore <= userGrade + 20) {
                    // 该专业为冲
                    schoolMajorVO.setCategory(2);
                    rush++;
                } else if (majorScore < userGrade - 10 && majorScore >= userGrade - 30) {
                    // 该专业为保
                    schoolMajorVO.setCategory(0);
                    minimum++;
                }
            }
            // 2.9将有分类的专业加入到SchoolMajorVOList中返回
            if (schoolMajorVO.getCategory() != null) {
                schoolMajorVOList.add(schoolMajorVO);
            }
        }
        // 3.构建ForecastVO结果
        ForecastVO forecastVO = new ForecastVO();
        // 3.1封装可选专业
        forecastVO.setSelectableMajor(schoolMajorList.size());
        // 3.2封装专业列表
        forecastVO.setMajorForecastList(schoolMajorVOList);
        // 3.3计算录取概率
        double chance = (stable + minimum + 0.2 * rush) / schoolMajorList.size();
        forecastVO.setChance((int) Math.round(chance * 100));
        return forecastVO;
    }

    @Override
    public List<BriefSchoolInfoVO> getHotSchool() {
        UserVO user = userMapper.selectById(ThreadLocalUtil.getCurrentId());
        List<BriefSchoolInfoVO> briefSchoolInfoVOList = new ArrayList<>();
        if (user != null) {
            List<School> schoolList = schoolMapper.selectByProvince(user.getProvince());
            for (School school : schoolList) {
                school.setRankList(school.getRankList().split(",")[1]);
                BriefSchoolInfoVO briefSchoolInfoVO = new BriefSchoolInfoVO();
                briefSchoolInfoVO.setName(school.getSchoolName());
                briefSchoolInfoVO.setHead(school.getSchoolHead());
                briefSchoolInfoVO.setRank(school.getRankList());
                briefSchoolInfoVOList.add(briefSchoolInfoVO);
            }
        } else {
            // 用户没有登录，展示默认的大学
            List<String> hotSchool = new ArrayList<>();
            Collections.addAll(hotSchool, "清华大学", "浙江大学", "四川大学", "中国科学技术大学", "中山大学", "哈尔滨工业大学",
                    "武汉大学", "厦门大学", "西安交通大学", "西南大学");
            for (String school : hotSchool) {
                School schoolInfo = schoolMapper.getByName(school);
                BriefSchoolInfoVO briefSchoolInfoVO = new BriefSchoolInfoVO();
                briefSchoolInfoVO.setName(schoolInfo.getSchoolName());
                briefSchoolInfoVO.setHead(schoolInfo.getSchoolHead());
                briefSchoolInfoVO.setRank(schoolInfo.getRankList().split(",")[1]);
                briefSchoolInfoVOList.add(briefSchoolInfoVO);
            }
        }
        return briefSchoolInfoVOList;
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
