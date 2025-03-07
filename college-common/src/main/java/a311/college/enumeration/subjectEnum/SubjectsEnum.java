package a311.college.enumeration.subjectEnum;


import lombok.Getter;

/**
 * 枚举定义高考选择科目
 */
@Getter
public enum SubjectsEnum {

    物理("物理"),
    化学("化学"),
    生物("生物"),
    历史("历史"),
    政治("政治"),
    地理("地理");

    private final String name;

    SubjectsEnum(String name) {
        this.name = name;
    }

}
