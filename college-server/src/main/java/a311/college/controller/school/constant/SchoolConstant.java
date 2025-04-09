package a311.college.controller.school.constant;

import a311.college.entity.major.Major;
import a311.college.vo.major.HotMajorVO;
import a311.college.vo.school.SchoolVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * School常量
 */
public class SchoolConstant {

    public static List<HotMajorVO> getHotMajor() {
        List<HotMajorVO> hotMajorVOList = new ArrayList<>();
        hotMajorVOList.add(new HotMajorVO("人工智能", "工学,计算机类", "清华大学", 140, 691));
        hotMajorVOList.add(new HotMajorVO("电子科学与技术", "工学,电子信息类", "电子科技大学", 661, 662));
        hotMajorVOList.add(new HotMajorVO("临床医学", "医学,临床医学类", "上海交通大学", 75, 688));
        hotMajorVOList.add(new HotMajorVO("法学", "法学,法学类", "中国政法大学", 569, 665));
        hotMajorVOList.add(new HotMajorVO("自动化", "工学,自动化类", "哈尔滨工业大学", 34, 664));
        hotMajorVOList.add(new HotMajorVO("数据科学与大数据技术", "工学,计算机类", "北京大学", 31, 680));
        hotMajorVOList.add(new HotMajorVO("能源与动力工程", "工学,能源动力类", "华中科技大学", 127, 658));
        hotMajorVOList.add(new HotMajorVO("物联网工程", "工学,电子信息类", "西安电子科技大学", 57, 645));
        hotMajorVOList.add(new HotMajorVO("软件工程", "工学,计算机类", "浙江大学", 114, 672));
        hotMajorVOList.add(new HotMajorVO("汉语言文学", "文学,中国语言文学类", "北京师范大学", 52, 643));
        return hotMajorVOList;
    }

    public static List<HotMajorVO> getHotProfessionalMajor() {
        List<HotMajorVO> hotMajorVOList = new ArrayList<>();
        hotMajorVOList.add(new HotMajorVO("护理", "医药卫生大类,护理类", "南昌职业大学", 909, 291));
        hotMajorVOList.add(new HotMajorVO("工业设计", "装备制造大类,机械设计类", "西安汽车职业大学", 2257, 383));
        hotMajorVOList.add(new HotMajorVO("学前教育", "教育与体育大类,教育类", "南昌职业大学", 909, 290));
        hotMajorVOList.add(new HotMajorVO("计算机网络技术", "电子与信息大类,计算机类", "深圳职业技术大学", 646, 504));
        hotMajorVOList.add(new HotMajorVO("数字媒体技术", "文化艺术大类,艺术设计类", "上海工艺美术职业学院", 671, 664));
        hotMajorVOList.add(new HotMajorVO("钻探工程技术", "资源环境与安全大类,地质类", "湖南工程职业技术学院", 2003, 423));
        return hotMajorVOList;
    }

    /**
     * 获取默认学校
     *
     * @return List<SchoolVO>
     */
    public static List<SchoolVO> getSchool() {
        List<SchoolVO> schoolVOList = new ArrayList<>();
        schoolVOList.add(new SchoolVO(31, "https://static-data.gaokao.cn/upload/logo/31.jpg", "北京大学", null, "北京市海淀区", "985,211,双一流"));
        schoolVOList.add(new SchoolVO(140, "https://static-data.gaokao.cn/upload/logo/140.jpg", "清华大学", null, "北京市海淀区", "985,211,双一流"));
        schoolVOList.add(new SchoolVO(114, "https://static-data.gaokao.cn/upload/logo/114.jpg", "浙江大学", null, "浙江杭州市", "985,211,双一流"));
        schoolVOList.add(new SchoolVO(132, "https://static-data.gaokao.cn/upload/logo/132.jpg", "复旦大学", null, "上海市杨浦区", "985,211,双一流"));
        schoolVOList.add(new SchoolVO(42, "https://static-data.gaokao.cn/upload/logo/42.jpg", "武汉大学", null, "湖北武汉市", "985,211,双一流"));
        return schoolVOList;
    }

    /**
     * 获取默认专业
     *
     * @return List<Major>
     */
    public static List<Major> getMajor() {
        List<Major> majorList = new ArrayList<>();
        majorList.add(new Major(1, "计算机科学与技术", null, null, null, "66:34", 14200));
        majorList.add(new Major(21, "人工智能", null, null, null, "70:30", 17200));
        majorList.add(new Major(19, "电子信息工程", null, null, null, "73:27", 12900));
        majorList.add(new Major(366, "汉语言文学", null, null, null, "16:84", 10900));
        majorList.add(new Major(274, "临床医学", null, null, null, "44:56", 13000));
        return majorList;
    }

    public static List<String> getHotSchoolList() {
        List<String> hotSchoolList = new ArrayList<>();
        Collections.addAll(hotSchoolList, "清华大学", "浙江大学", "四川大学", "中国科学技术大学", "中山大学", "哈尔滨工业大学",
                "武汉大学", "厦门大学", "西安交通大学", "重庆文理学院");
        return hotSchoolList;
    }

    public static List<SchoolVO> getHotSchool() {
        List<SchoolVO> schoolVOList = new ArrayList<>();
        schoolVOList.add(new SchoolVO(102, "https://static-data.gaokao.cn/upload/logo/102.jpg", "厦门大学", null, null, null));
        schoolVOList.add(new SchoolVO(99, "https://static-data.gaokao.cn/upload/logo/99.jpg", "四川大学", null, null, null));
        schoolVOList.add(new SchoolVO(42, "https://static-data.gaokao.cn/upload/logo/42.jpg", "武汉大学", null, null, null));
        schoolVOList.add(new SchoolVO(104, "https://static-data.gaokao.cn/upload/logo/104.jpg", "中山大学", null, null, null));
        schoolVOList.add(new SchoolVO(140, "https://static-data.gaokao.cn/upload/logo/140.jpg", "清华大学", null, null, null));
        schoolVOList.add(new SchoolVO(123, "https://static-data.gaokao.cn/upload/logo/123.jpg", "中南大学", null, null, null));
        schoolVOList.add(new SchoolVO(111, "https://static-data.gaokao.cn/upload/logo/111.jpg", "南京大学", null, null, null));
        schoolVOList.add(new SchoolVO(934, "https://static-data.gaokao.cn/upload/logo/934.jpg", "西南大学", null, null, null));
        schoolVOList.add(new SchoolVO(31, "https://static-data.gaokao.cn/upload/logo/31.jpg", "北京大学", null, null, null));
        schoolVOList.add(new SchoolVO(661, "https://static-data.gaokao.cn/upload/logo/661.jpg", "电子科技大学", null, null, null));
        return schoolVOList;
    }


}
