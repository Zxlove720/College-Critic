package a311.college.service.impl;

import a311.college.constant.error.SchoolErrorConstant;
import a311.college.constant.user.UserErrorConstant;
import a311.college.dto.major.MajorDTO;
import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.MajorSchoolPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.dto.query.school.CommentPageQueryDTO;
import a311.college.dto.school.AddCommentDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.entity.school.School;
import a311.college.exception.CommentIllegalException;
import a311.college.exception.PageQueryException;
import a311.college.exception.ReAdditionException;
import a311.college.filter.FinderUtil;
import a311.college.mapper.major.MajorMapper;
import a311.college.mapper.school.SchoolMapper;
import a311.college.result.PageResult;
import a311.college.service.MajorService;
import a311.college.thread.ThreadLocalUtil;
import a311.college.vo.major.DetailMajorVO;
import a311.college.vo.school.CommentVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    private final SchoolMapper schoolMapper;

    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper, SchoolMapper schoolMapper) {
        this.majorMapper = majorMapper;
        this.schoolMapper = schoolMapper;
    }

    /**
     * 查询学科门类
     *
     * @param subjectCategoryQueryDTO 学科门类查询DTO
     * @return List<SubjectCategory>
     */
    @Override
    public List<SubjectCategory> getSubjectCategory(SubjectCategoryQueryDTO subjectCategoryQueryDTO) {
        return majorMapper.selectSubjectCategory(subjectCategoryQueryDTO);
    }

    /**
     * 获取专业类别
     *
     * @param professionalClassQueryDTO 专业类别查询DTO
     * @return List<ProfessionalClass>
     */
    @Override
    public List<ProfessionalClass> getProfessionalClass(ProfessionalClassQueryDTO professionalClassQueryDTO) {
        return majorMapper.selectProfessionalClass(professionalClassQueryDTO);
    }

    /**
     * 专业分页查询
     *
     * @param majorPageQueryDTO 专业分页查询DTO
     * @return PageResult<Major>
     */
    @Override
    public PageResult<Major> majorPageQuery(MajorPageQueryDTO majorPageQueryDTO) {
        try (Page<Major> page = PageHelper.startPage(majorPageQueryDTO.getPage(), majorPageQueryDTO.getPageSize())) {
            majorMapper.pageQueryMajors(majorPageQueryDTO);
            // 获取专业记录数
            long total = page.getTotal();
            // 获取总记录
            List<Major> result = page.getResult();
            return new PageResult<>(total, result);
        } catch (Exception e) {
            log.error("专业分页查询失败，报错为：{}", e.getMessage());
            throw new PageQueryException(e.getMessage());
        }
    }

    /**
     * 查询专业具体信息
     *
     * @param majorDTO 专业DTO
     * @return DetailMajorVO
     */
    @Override
    public DetailMajorVO getDetailMajor(MajorDTO majorDTO) {
        // 1.根据id查询到对应专业
        Major major = majorMapper.selectById(majorDTO.getMajorId());
        // 2.封装专业详情对象
        DetailMajorVO detailMajorVO = new DetailMajorVO();
        BeanUtil.copyProperties(major, detailMajorVO);
        // 3.构造专业满意度集合
        List<Double> satisfaction = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            // 3.1设置随机满意度
            double randomSatisfaction = Math.round(RandomUtil.randomDouble(3.7, 5.0) * 10) / 10.0;
            satisfaction.add(randomSatisfaction);
        }
        // 4.构造专业就业率集合
        List<String> employmentRate = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // 4.1构造随机就业率集合
            int start = RandomUtil.randomInt(75, 81);
            int end = RandomUtil.randomInt(81, 93);
            String rate = start + "%" + "~" + end + "%";
            employmentRate.add(rate);
        }
        // 5.返回专业详情对象
        detailMajorVO.setSatisfaction(satisfaction);
        detailMajorVO.setEmploymentRate(employmentRate);
        return detailMajorVO;
    }

    /**
     * 用户收藏专业
     *
     * @param majorDTO 专业DTO
     */
    @Override
    public void addFavoriteMajor(MajorDTO majorDTO) {
        long userId = ThreadLocalUtil.getCurrentId();
        majorDTO.setUserId(userId);
        if (majorMapper.checkMajorDistinct(majorDTO) != 0) {
            throw new ReAdditionException(UserErrorConstant.RE_ADDITION);
        }
        majorMapper.addFavoriteMajor(majorDTO);
    }

    /**
     * 用户删除收藏
     *
     * @param majorDTO 大学DTO
     */
    @Override
    public void deleteFavoriteMajor(MajorDTO majorDTO) {
        long userId = ThreadLocalUtil.getCurrentId();
        majorDTO.setUserId(userId);
        majorMapper.deleteFavoriteMajor(majorDTO);
    }

    /**
     * 查询某专业开设学校
     *
     * @param majorSchoolPageQueryDTO 专业分页查询DTO
     * @return List<School>
     */
    @Override
    public PageResult<School> querySchools(MajorSchoolPageQueryDTO majorSchoolPageQueryDTO) {
        try (Page<School> page = PageHelper.startPage(majorSchoolPageQueryDTO.getPage(), majorSchoolPageQueryDTO.getPageSize())) {
            schoolMapper.selectMajorSchool(majorSchoolPageQueryDTO);
            return new PageResult<>(page.getTotal(), page.getResult());
        } catch (Exception e) {
            log.error("学校分页查询失败，报错为：{}", e.getMessage());
            throw new PageQueryException(e.getMessage());
        }
    }

    /**
     * 用户评价专业
     *
     * @param addCommentDTO 用户评价DTO
     */
    @Override
    public void addMajorComment(AddCommentDTO addCommentDTO) {
        // 进行敏感词判断
        if (FinderUtil.replace(addCommentDTO.getComment(), '*')) {
            throw new CommentIllegalException("评论不合法");
        }
        addCommentDTO.setUserId(ThreadLocalUtil.getCurrentId());
        addCommentDTO.setTime(LocalDateTime.now());
        addCommentDTO.setMajorName(majorMapper.selectById(addCommentDTO.getMajorId()).getMajorName());
        majorMapper.addComment(addCommentDTO);
    }

    /**
     * 分页查询用户评价
     *
     * @param commentPageQueryDTO 评论区分页查询DTO
     * @return List<CommentVO>
     */
    @Override
    public PageResult<CommentVO> showComment(CommentPageQueryDTO commentPageQueryDTO) {
        try (Page<CommentVO> page = PageHelper.startPage(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize())) {
            List<CommentVO> commentVOList = majorMapper.selectComment(commentPageQueryDTO.getMajorId());
            return new PageResult<>(page.getTotal(), commentVOList);
        } catch (Exception e) {
            log.error("'{}'大学评论区查询失败，报错为：{}", commentPageQueryDTO.getMajorId(), e.getMessage());
            throw new PageQueryException(SchoolErrorConstant.COMMENT_PAGE_QUERY_ERROR);
        }
    }


}
