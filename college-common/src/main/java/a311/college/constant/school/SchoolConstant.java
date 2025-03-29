package a311.college.constant.school;

import a311.college.vo.major.HotMajorVO;

import java.util.ArrayList;
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

    public static List<HotMajorVO> getHotMajor(int status) {
        List<HotMajorVO> hotMajorVOList = new ArrayList<>();
        hotMajorVOList.add(new HotMajorVO("护理", "医药卫生大类,护理类", "南昌职业大学", 909, 291));
        hotMajorVOList.add(new HotMajorVO("工业设计", "装备制造大类,机械设计类", "西安汽车职业大学", 2257, 383));
        hotMajorVOList.add(new HotMajorVO("学前教育", "教育与体育大类,教育类", "南昌职业大学", 909, 290));
        hotMajorVOList.add(new HotMajorVO("计算机网络技术", "电子与信息大类,计算机类", "深圳职业技术大学", 646, 504));
        hotMajorVOList.add(new HotMajorVO("数字媒体技术", "文化艺术大类,艺术设计类", "上海工艺美术职业学院", 671, 664));
        hotMajorVOList.add(new HotMajorVO("钻探工程技术", "资源环境与安全大类,地质类", "湖南工程职业技术学院", 2003, 423));
        return hotMajorVOList;
    }
}
