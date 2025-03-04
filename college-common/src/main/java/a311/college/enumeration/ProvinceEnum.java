package a311.college.enumeration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonDeserialize(using = ProvinceEnum.ProvinceEnumDeserializer.class)
public enum ProvinceEnum {

    // 枚举中字段有三个属性：省份名，客观得分，状态（1为新高考、0为老高考）

    HEILONGJIANG("黑龙江", 68, 1),    // 第四批新高考（2024年）
    JILIN("吉林", 70, 1),            // 第四批新高考（2024年）
    LIAONING("辽宁", 73, 1),         // 第三批新高考（2021年）
    SHANXI("山西", 65, 0),           // 第五批新高考（2025年，暂标记为0）
    HEBEI("河北", 75, 1),            // 第三批新高考（2021年）
    SHANDONG("山东", 80, 1),         // 第二批新高考（2020年）
    HENAN("河南", 68, 0),            // 第五批新高考（2025年，暂标记为0）
    JIANGSU("江苏", 90, 1),          // 第三批新高考（2021年）
    ZHEJIANG("浙江", 90, 1),         // 第一批新高考（2017年）
    ANHUI("安徽", 73, 1),            // 第四批新高考（2024年）
    FUJIAN("福建", 82, 1),           // 第三批新高考（2021年）
    GUANGDONG("广东", 90, 1),        // 第三批新高考（2021年）
    HUNAN("湖南", 75, 1),            // 第三批新高考（2021年）
    HUBEI("湖北", 78, 1),            // 第三批新高考（2021年）
    JIANGXI("江西", 70, 1),          // 第四批新高考（2024年）
    GANSU("甘肃", 58, 1),            // 第四批新高考（2024年）
    QINGHAI("青海", 55, 0),          // 第五批新高考（2025年，暂标记为0）
    SHAANXI("陕西", 78, 0),          // 第五批新高考（2025年，暂标记为0）
    SICHUAN("四川", 81, 0),          // 第五批新高考（2025年，暂标记为0）
    GUIZHOU("贵州", 67, 1),          // 第四批新高考（2024年）
    YUNNAN("云南", 65, 0),           // 第五批新高考（2025年，暂标记为0）
    HAINAN("海南", 75, 1),           // 第二批新高考（2020年）
    BEIJING("北京", 98, 1),          // 第二批新高考（2020年）
    TIANJIN("天津", 86, 1),          // 第二批新高考（2020年）
    SHANGHAI("上海", 95, 1),         // 第一批新高考（2017年）
    CHONGQING("重庆", 80, 1),        // 第三批新高考（2021年）
    NEIMENGGU("内蒙古", 63, 0),      // 第五批新高考（2025年，暂标记为0）
    GUANGXI("广西", 71, 1),          // 第四批新高考（2024年）
    XINJIANG("新疆", 58, 0),         // 老高考
    XIZANG("西藏", 50, 0),           // 老高考
    NINGXIA("宁夏", 63, 0);          // 第五批新高考（2025年，暂标记为0）

    private final String name;
    private final Integer score;
    private final Integer status;

    ProvinceEnum(String name, int score, int status) {
        this.name = name;
        this.score = score;
        this.status = status;
    }

    public static class ProvinceEnumDeserializer extends JsonDeserializer<ProvinceEnum> {

        @Override
        public ProvinceEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String provinceName = jsonParser.getText();
            for (ProvinceEnum province : ProvinceEnum.values()) {
                if (province.getName().equals(provinceName)) {
                    return province;
                }
            }
            throw new IllegalArgumentException("无法识别的省份名: " + provinceName);
        }
    }

}
