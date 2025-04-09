package a311.college.service.impl;

import a311.college.constant.error.SchoolErrorConstant;
import a311.college.constant.redis.SchoolRedisKey;
import a311.college.constant.user.UserErrorConstant;
import a311.college.controller.school.constant.SchoolConstant;
import a311.college.dto.query.PageQueryDTO;
import a311.college.dto.query.school.SchoolCommentPageQueryDTO;
import a311.college.dto.query.school.SchoolNameQueryDTO;
import a311.college.dto.school.*;
import a311.college.dto.query.school.GradePageQueryDTO;
import a311.college.dto.query.school.YearScoreQueryDTO;
import a311.college.dto.user.UserSearchDTO;
import a311.college.entity.school.School;
import a311.college.entity.school.SchoolMajor;
import a311.college.exception.PageQueryException;
import a311.college.exception.ReAdditionException;
import a311.college.mapper.major.MajorMapper;
import a311.college.mapper.resource.ResourceMapper;
import a311.college.mapper.school.SchoolMapper;
import a311.college.result.PageResult;
import a311.college.service.SchoolService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.major.BriefMajorVO;
import a311.college.vo.major.HotMajorVO;
import a311.college.vo.major.MajorSimpleVO;
import a311.college.vo.school.*;
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
import java.util.stream.Collectors;

/**
 * 大学相关服务实现类
 */
@Slf4j
@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolMapper schoolMapper;

    private final ResourceMapper resourceMapper;

    private final MajorMapper majorMapper;

    @Autowired
    public SchoolServiceImpl(SchoolMapper schoolMapper, ResourceMapper resourceMapper, MajorMapper majorMapper) {
        this.schoolMapper = schoolMapper;
        this.resourceMapper = resourceMapper;
        this.majorMapper = majorMapper;
    }

    @Resource
    private RedisTemplate<String, SchoolVO> redisTemplate;

    /**
     * 大学信息分页查询
     *
     * @param schoolPageQueryDTO 大学分页查询DTO
     * @return PageResult<DetailedSchoolVO>
     */
    @Override
    public PageResult<SchoolVO> pageSelect(SchoolPageQueryDTO schoolPageQueryDTO) {
        String key = SchoolRedisKey.SCHOOL_CACHE_KEY + schoolPageQueryDTO.getProvince() + ":";
        List<SchoolVO> schoolCache = redisTemplate.opsForList().range(key, 0, -1);
        if (schoolCache != null && !schoolCache.isEmpty()) {
            log.info("缓存命中");
            List<SchoolVO> filterCache = filterSchools(schoolCache, schoolPageQueryDTO);
            return manualPage(filterCache, schoolPageQueryDTO.getPage(), schoolPageQueryDTO.getPageSize());
        }
        log.info("缓存未命中，开启分页查询");
        try (Page<SchoolVO> page = PageHelper.startPage(schoolPageQueryDTO.getPage(), schoolPageQueryDTO.getPageSize())) {
            schoolMapper.pageQuery(schoolPageQueryDTO);
            // 获取总记录数
            long total = page.getTotal();
            // 获取总记录
            List<SchoolVO> result = page.getResult();
            if (!result.isEmpty()) {
                // 将其添加到缓存
                redisTemplate.opsForList().rightPushAll(key, result);
                redisTemplate.expire(key, SchoolRedisKey.SCHOOL_CACHE_TTL, TimeUnit.SECONDS);
            }
            return new PageResult<>(total, result);
        } catch (Exception e) {
            log.error("大学信息分页查询失败，报错为：{}", e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.SCHOOL_PAGE_QUERY_ERROR);
        }
    }

    private PageResult<SchoolVO> manualPage(List<SchoolVO> filterCache, Integer page, Integer pageSize) {
        int total = filterCache.size();
        int start = (page - 1) * pageSize;
        if (start >= total) return new PageResult<>((long) total, Collections.emptyList());
        int end = Math.min(start + pageSize, total);
        List<SchoolVO> pageData = filterCache.subList(start, end);
        return new PageResult<>((long) total, pageData);
    }

    private List<SchoolVO> filterSchools(List<SchoolVO> schoolCache, SchoolPageQueryDTO schoolPageQueryDTO) {
        return schoolCache.stream()
                .filter(s -> schoolPageQueryDTO.getSchoolName() == null || s.getSchoolName().contains(schoolPageQueryDTO.getSchoolName()))
                .filter(s -> schoolPageQueryDTO.getRankList() == null || schoolPageQueryDTO.getRankList().toString().contains(s.getRankList()))
                .filter(s -> schoolPageQueryDTO.getProvince() == null || s.getSchoolProvince().getName().contains(schoolPageQueryDTO.getProvince()))
                .collect(Collectors.toList());
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
                List<SchoolVO> school = schoolMapper.selectByAddress(area);
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
     * 大学专业分页查询
     *
     * @param schoolMajorPageQueryDTO 大学专业分页查询DTO
     * @return PageResult<SchoolMajor>
     */
    @Override
    public PageResult<SchoolMajor> pageSelectMajor(SchoolMajorPageQueryDTO schoolMajorPageQueryDTO) {
        try (Page<SchoolMajor> page = PageHelper.startPage(schoolMajorPageQueryDTO.getPage(), schoolMajorPageQueryDTO.getPageSize())) {
            schoolMapper.pageQuerySchoolMajor(schoolMajorPageQueryDTO);
            long total = page.getTotal();
            List<SchoolMajor> result = page.getResult();
            return new PageResult<>(total, result);
        } catch (Exception e) {
            log.error("大学专业分页查询失败，报错为：{}", e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.SCHOOL_MAJOR_PAGE_QUERY_ERROR);
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
        return schoolMapper.searchBySchoolName(schoolNameQueryDTO.getSchoolName());
    }

    /**
     * 用户搜索提示
     *
     * @param userSearchDTO 用户搜索DTO
     * @return SearchVO
     */
    @Override
    public SearchVO search(UserSearchDTO userSearchDTO) {
        // 1.用户搜索，先返回学校信息
        List<BriefSchoolInfoVO> briefSchoolInfoVOList = schoolMapper.searchSchool(userSearchDTO.getMessage());
        // 2.用户搜索，再返回专业信息
        List<BriefMajorVO> briefMajorVOList = majorMapper.searchMajor(userSearchDTO.getMessage());
        // 3.若无匹配的学校（用户输入错误）
        if (briefSchoolInfoVOList == null || briefSchoolInfoVOList.isEmpty()) {
            log.info("用户'{}'，没有搜索到学校信息，返回默认学校信息", ThreadLocalUtil.getCurrentId());
            // 3.1返回固定的学校信息
            briefSchoolInfoVOList = SchoolConstant.getSchool();
        } else {
            // 3.2成功匹配到学校数据，对其进行处理
            for (BriefSchoolInfoVO briefSchoolInfoVO : briefSchoolInfoVOList) {
                String[] split = briefSchoolInfoVO.getRankList().split(",");
                StringBuilder rank = new StringBuilder(split[0]);
                if (split.length == 3) {
                    rank.append(split[1]).append(split[2]);
                }
                if (split.length > 3) {
                    rank.append(split[2]).append(split[3]);
                }
                briefSchoolInfoVO.setRankList(rank.toString());
            }
        }
        // 4.若无匹配的专业（用户输入错误）
        if (briefMajorVOList == null || briefMajorVOList.isEmpty()) {
            log.info("用户'{}'，没有搜索到专业信息，返回默认专业信息", ThreadLocalUtil.getCurrentId());
            // 4.1返回固定的专业信息
            briefMajorVOList = SchoolConstant.getMajor();
        } else {
            // 4.2成功匹配到专业数据，对其进行处理
            for (BriefMajorVO briefMajorVO : briefMajorVOList) {
                briefMajorVO.setGender(briefMajorVO.getGender().equals("--") ? null : briefMajorVO.getGender());
                briefMajorVO.setAvgSalary(briefMajorVO.getAvgSalary().equals("0") ? null : briefMajorVO.getAvgSalary());
                briefMajorVO.setInformation(briefMajorVO.getGender() + "," + briefMajorVO.getAvgSalary());
            }
        }
        return new SearchVO(briefSchoolInfoVOList, briefMajorVOList);
    }

    /**
     * 根据用户成绩查询大学
     *
     * @param gradePageQueryDTO 用户成绩DTO
     * @return List<School>
     */
    @Override
    public PageResult<School> getSchoolByGrade(GradePageQueryDTO gradePageQueryDTO) {
        try (Page<School> page = PageHelper.startPage(gradePageQueryDTO.getPage(), gradePageQueryDTO.getPageSize())) {
            List<School> schoolList = schoolMapper.selectByGrade(gradePageQueryDTO);
            return new PageResult<>(page.getTotal(), schoolList);
        } catch (Exception e) {
            log.error("按照成绩分页查询失败，错误信息：{}", e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.SCHOOL_GRADE_PAGE_QUERY_ERROR);
        }
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
        List<MajorSimpleVO> majorSimpleVOList = schoolMapper.selectMajor(school.getSchoolId());
        // 5.2调整专业格式
        for (MajorSimpleVO majorSimpleVO : majorSimpleVOList) {
            majorSimpleVO.setMajorName(majorSimpleVO.getMajorName());
        }
        // 5.3封装专业列表
        detailedSchoolVO.setMajors(majorSimpleVOList);
        // 6.判断该学校是否被用户收藏
        schoolDTO.setUserId(ThreadLocalUtil.getCurrentId());
        detailedSchoolVO.setFavorite(schoolMapper.checkSchoolDistinct(schoolDTO) == 1);
        return detailedSchoolVO;
    }

    /**
     * 为好学校设置学校设施
     */
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

    /**
     * 为有钱的学校设置学校设施
     */
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

    /**
     * 为一般学校设置学校设施
     */
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

    /**
     * 用户收藏大学
     *
     * @param schoolDTO 大学DTO
     */
    @Override
    public void addFavoriteSchool(SchoolDTO schoolDTO) {
        long userId = ThreadLocalUtil.getCurrentId();
        schoolDTO.setUserId(userId);
        if (schoolMapper.checkSchoolDistinct(schoolDTO) != 0) {
            throw new ReAdditionException(UserErrorConstant.RE_ADDITION);
        }
        schoolMapper.addFavoriteSchool(schoolDTO);
    }

    /**
     * 用户删除收藏
     *
     * @param schoolDTO 大学DTO
     */
    @Override
    public void deleteFavoriteSchool(SchoolDTO schoolDTO) {
        long userId = ThreadLocalUtil.getCurrentId();
        schoolDTO.setUserId(userId);
        schoolMapper.deleteFavoriteSchool(schoolDTO);
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
        List<SchoolMajor> schoolMajorList = schoolMapper.selectAllMajor(forecastDTO);
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
     * 分页查询用户评价
     *
     * @param schoolCommentPageQueryDTO 大学DTO
     * @return List<CommentVO>
     */
    @Override
    public PageResult<CommentVO> showComment(SchoolCommentPageQueryDTO schoolCommentPageQueryDTO) {
        try (Page<CommentVO> page = PageHelper.startPage(schoolCommentPageQueryDTO.getPage(), schoolCommentPageQueryDTO.getPageSize())) {
            List<CommentVO> commentVOList = schoolMapper.selectComment(schoolCommentPageQueryDTO.getSchoolId());
            return new PageResult<>(page.getTotal(), commentVOList);
        } catch (Exception e) {
            log.error("'{}'大学评论区查询失败，报错为：{}", schoolCommentPageQueryDTO.getSchoolId(), e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.COMMENT_PAGE_QUERY_ERROR);
        }
    }

    /**
     * 获取热门专业（本科）
     *
     * @return List<HotMajorVO>
     */
    @Override
    public List<HotMajorVO> getHotMajor() {
        return SchoolConstant.getHotMajor();
    }

    /**
     * 获取热门专业（专科）
     *
     * @return List<HotMajorVO>
     */
    @Override
    public List<HotMajorVO> getHotMajorProfessional() {
        return SchoolConstant.getHotProfessionalMajor();
    }

    /**
     * 分页查询双一流学校
     *
     * @param pageQueryDTO 分页查询DTO
     * @return PageResult<BriefSchoolInfoVO>
     */
    @Override
    public PageResult<School> getClassicSchool(PageQueryDTO pageQueryDTO) {
        try (Page<School> page = PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize())) {
            schoolMapper.selectClassicSchool();
            return new PageResult<>(page.getTotal(), page.getResult());
        } catch (Exception e) {
            log.error("双一流大学查询失败，错误信息为：{}", e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.CLASSIC_SCHOOL_PAGE_QUERY_ERROR);
        }

    }

    /**
     * 获取热门院校
     *
     * @return List<BriefSchoolInfoVO>
     */
    @Override
    public List<HotSchoolVO> getHotSchool() {
        return SchoolConstant.getHotSchool();
    }

    /**
     * 首页获取校园风光
     *
     * @return List<SchoolSceneryVO>
     */
    @Override
    public List<SchoolSceneryVO> getScenery() {
        return schoolMapper.selectScenery();
    }
}
