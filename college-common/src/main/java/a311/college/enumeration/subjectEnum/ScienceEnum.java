package a311.college.enumeration.subjectEnum;

import lombok.Getter;

/**
 * 理科
 */
@Getter
public enum ScienceEnum {

    物理("物理"),
    化学("化学"),
    生物("生物");

    private final String name;

    ScienceEnum(String name) {
        this.name = name;
    }

}
