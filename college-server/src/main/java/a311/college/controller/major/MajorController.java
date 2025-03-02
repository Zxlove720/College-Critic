package a311.college.controller.major;

import a311.college.result.Result;
import a311.college.service.MajorService;
import a311.college.vo.MajorVO;
import a311.college.vo.SubjectCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 专业Controller
 */
@RestController
@RequestMapping("/majors")
public class MajorController {

    private final MajorService majorService;

    @Autowired
    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    /**
     * 根据培养层次获取学科门类
     * @param id 培养层次id
     * @return Result<List<SubjectCategoryVO>>
     */
    @GetMapping("/category")
    public Result<List<SubjectCategoryVO>> getSubjectCategory(int id) {
        return Result.success(majorService.getSubjectCategory(id));
    }


    /**
     * 根据培养层次获取专业
     * @param id 层次id
     * @return Result<Void>
     */
    @GetMapping("/{id}")
    public Result<List<MajorVO>> getByLevel(@PathVariable int id) {
        return Result.success(majorService.getByLevel(id));
    }

}
