package a311.college.address;

import a311.college.enumeration.ProvinceEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddressToEnumUtil {

    private static final List<String> PROVINCES = Arrays.stream(ProvinceEnum.values())
            .map(ProvinceEnum::getName)
            .toList();


    public static String extractProvince(String address) {
        for (String province : PROVINCES) {
            if (address.startsWith(province) ||
                    address.startsWith(province + "自治区") ||
                    address.startsWith(province + "省")) {
                return province;
            }
        }
        throw new IllegalArgumentException("地址中未找到匹配省份: " + address);
    }

    public static ProvinceEnum toProvinceEnum(String address) {
        String provinceName = extractProvince(address);
        return Arrays.stream(ProvinceEnum.values())
                .filter(e -> e.getName().equals(provinceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效的省份: " + provinceName));
    }
}





