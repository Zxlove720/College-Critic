package a311.college.boot;

import a311.college.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BootTask implements ApplicationRunner {

    private final SchoolService schoolService;

    @Autowired
    public BootTask(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    /**
     * 缓存预热
     *
     */
    @Override
    public void run(ApplicationArguments arguments) {
        try {
            schoolService.cacheSchool();
            schoolService.cacheHot();
        } catch (Exception e) {
            System.out.println("缓存预热失败");
        }
    }
}
