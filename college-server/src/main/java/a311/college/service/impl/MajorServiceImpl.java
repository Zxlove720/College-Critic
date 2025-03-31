package a311.college.service.impl;

import a311.college.dto.query.major.MajorPageQueryDTO;
import a311.college.dto.query.major.ProfessionalClassQueryDTO;
import a311.college.dto.query.major.SubjectCategoryQueryDTO;
import a311.college.entity.major.Major;
import a311.college.entity.major.ProfessionalClass;
import a311.college.entity.major.SubjectCategory;
import a311.college.mapper.major.MajorMapper;
import a311.college.result.PageResult;
import a311.college.service.MajorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
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
            return null;
        }
    }

    /**
     * 根据专业名搜索专业
     *
     * @param majorName 专业名
     * @return List<Major>
     */
    @Override
    public List<Major> searchMajorByName(String majorName) {
        return majorMapper.searchMajorByName(majorName);
    }


}
