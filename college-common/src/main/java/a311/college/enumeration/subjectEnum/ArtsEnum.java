package a311.college.enumeration.subjectEnum;

import lombok.Getter;

/**
 * 文科
 */
@Getter
public enum ArtsEnum {

    历史("历史"),
    政治("政治"),
    地理("地理");

    private final String name;

    ArtsEnum(String name) {
        this.name = name;
    }
}
