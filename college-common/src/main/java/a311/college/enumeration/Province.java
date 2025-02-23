package a311.college.enumeration;

import lombok.Getter;

@Getter
public enum Province {
    HEILONGJIANG("黑龙江", 68),
    JILIN("吉林", 70),
    LIAONING("辽宁", 73),
    SHANXI("山西", 65),
    HEBEI("河北", 75),
    SHANDONG("山东", 80),
    HENAN("河南", 68),
    JIANGSU("江苏", 90),
    ZHEJIANG("浙江", 90),
    ANHUI("安徽", 73),
    FUJIAN("福建", 82),
    GUANGDONG("广东", 90),
    HUNAN("湖南", 75),
    HUBEI("湖北", 78),
    JIANGXI("江西", 70),
    GANSU("甘肃", 58),
    QINGHAI("青海", 55),
    SHAANXI("陕西", 78),
    SICHUAN("四川", 81),
    GUIZHOU("贵州", 67),
    YUNNAN("云南", 65),
    HAINAN("海南", 75),
    BEIJING("北京", 98),
    TIANJIN("天津", 86),
    SHANGHAI("上海", 95),
    CHONGQING("重庆", 80),
    NEIMENGGU("内蒙古", 63),
    GUANGXI("广西", 71),
    XINJIANG("新疆", 58),
    XIZANG("西藏", 50),
    NINGXIA("宁夏", 63);

    private final String name;

    Province(String name, int score) {
        this.name = name;
    }

}
