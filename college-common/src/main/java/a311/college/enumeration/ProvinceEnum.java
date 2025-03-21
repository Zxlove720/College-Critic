package a311.college.enumeration;

import lombok.Getter;


@Getter
public enum ProvinceEnum {
    // 枚举中字段有三个属性：省份名，客观得分，状态（1为新高考、0为老高考）
    黑龙江("黑龙江", 68, 1),
    吉林("吉林", 70, 1),
    辽宁("辽宁", 73, 1),
    山西("山西", 65, 0),
    河北("河北", 75, 1),
    山东("山东", 80, 1),
    河南("河南", 68, 0),
    江苏("江苏", 90, 1),
    浙江("浙江", 90, 1),
    安徽("安徽", 73, 1),
    福建("福建", 82, 1),
    广东("广东", 90, 1),
    湖南("湖南", 75, 1),
    湖北("湖北", 78, 1),
    江西("江西", 70, 1),
    甘肃("甘肃", 58, 1),
    青海("青海", 55, 0),
    陕西("陕西", 78, 0),
    四川("四川", 81, 0),
    贵州("贵州", 67, 1),
    云南("云南", 65, 0),
    海南("海南", 75, 1),
    北京("北京", 98, 1),
    天津("天津", 86, 1),
    上海("上海", 95, 1),
    重庆("重庆", 80, 1),
    内蒙古("内蒙古", 63, 0),
    广西("广西", 71, 1),
    新疆("新疆", 58, 0),
    西藏("西藏", 50, 0),
    宁夏("宁夏", 63, 0);

    private final String name;
    private final Integer score;
    private final Integer status;

    ProvinceEnum(String name, int score, int status) {
        this.name = name;
        this.score = score;
        this.status = status;
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
