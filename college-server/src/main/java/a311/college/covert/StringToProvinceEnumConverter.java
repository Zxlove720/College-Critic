package a311.college.covert;

import a311.college.enumeration.ProvinceEnum;
import org.springframework.core.convert.converter.Converter;

public class StringToProvinceEnumConverter implements Converter<String, ProvinceEnum> {

    @Override
    public ProvinceEnum convert(String source) {
        for (ProvinceEnum province : ProvinceEnum.values()) {
            if (province.getName().equals(source)) {
                return province;
            }
        }
        throw new IllegalArgumentException("无效省份名" + source);
    }
}
