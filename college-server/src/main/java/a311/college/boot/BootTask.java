//package a311.college.boot;
//
//import a311.college.service.CollegeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BootTask implements ApplicationRunner {
//
//    private final CollegeService collegeService;
//
//    @Autowired
//    public BootTask(CollegeService collegeService) {
//        this.collegeService = collegeService;
//    }
//
//    /**
//     * 缓存预热
//     *
//     * @param arguments
//     */
//    @Override
//    public void run(ApplicationArguments arguments) {
//        try {
//            collegeService.cacheCollege();
//        } catch (Exception e) {
//            System.out.println("缓存预热失败");
//        }
//    }
//}
