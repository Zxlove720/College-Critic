package a311.college.enumeration;

import lombok.Getter;


@Getter
public enum ProvinceEnum {
    // 枚举中字段有三个属性：省份名，客观得分
    黑龙江("黑龙江", 68, "哈尔滨工业大学", "黑龙江农业工程职业学院"),
    吉林("吉林", 70, "吉林大学", "长春医学高等专科学校"),
    辽宁("辽宁", 73, "东北大学", "沈阳职业技术学院"),
    山西("山西", 65, "太原理工大学", "山西工程职业学院"),
    河北("河北", 75, "河北大学", "石家庄铁路职业技术学院"),
    山东("山东", 80, "山东大学", "山东职业学院"),
    河南("河南", 68, "郑州大学", "河南工业职业技术学院"),
    江苏("江苏", 90, "南京大学", "江苏建筑职业技术学院"),
    浙江("浙江", 90, "浙江大学", "浙江商业职业技术学院"),
    安徽("安徽", 73, "中国科学技术大学", "安徽医学高等专科学校"),
    福建("福建", 82, "厦门大学", "漳州职业技术学院"),
    广东("广东", 90, "中山大学", "广东轻工职业技术大学"),
    湖南("湖南", 75, "湖南大学", "湖南交通职业技术学院"),
    湖北("湖北", 78, "华中科技大学", "武汉铁路职业技术学院"),
    江西("江西", 70, "南昌大学", "江西财经职业学院"),
    甘肃("甘肃", 58, "兰州大学", "甘肃交通职业技术学院"),
    青海("青海", 55, "青海大学", "青海卫生职业技术学院"),
    陕西("陕西", 78, "西安交通大学", "西安航空职业技术学院"),
    四川("四川", 81, "电子科技大学", "成都职业技术学院"),
    贵州("贵州", 67, "贵州大学", "贵州轻工职业技术学院"),
    云南("云南", 65, "云南大学", "昆明冶金高等专科学校"),
    海南("海南", 75, "海南大学", "海南外国语职业学院"),
    北京("北京", 98, "清华大学", "北京电子科技职业学院"),
    天津("天津", 86, "南开大学", "天津市职业大学"),
    上海("上海", 95, "上海交通大学", "上海电子信息职业技术学院"),
    重庆("重庆", 80, "重庆大学", "重庆电力高等专科学校"),
    内蒙古("内蒙古", 63, "内蒙古大学", null),
    广西("广西", 71, "广西大学", "柳州城市职业学院"),
    新疆("新疆", 58, "新疆大学", "新疆轻工职业技术学院"),
    西藏("西藏", 50, "西藏大学", "西藏职业技术学院"),
    宁夏("宁夏", 63, "宁夏大学", null);

    private final String name;
    private final Integer score;
    private final String bestSchool;
    private final String bestProfessional;

    ProvinceEnum(String name, int score, String bestSchool, String bestProfessional) {
        this.name = name;
        this.score = score;
        this.bestSchool = bestSchool;
        this.bestProfessional = bestProfessional;
    }

    public static ProvinceEnum getProvince(String province) {
        for (ProvinceEnum value : ProvinceEnum.values()) {
            if (province.equals(value.getName())) {
                return value;
            }
        }
        return null;
    }
}
