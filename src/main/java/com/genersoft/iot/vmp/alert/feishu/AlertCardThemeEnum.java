package com.genersoft.iot.vmp.alert.feishu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertCardThemeEnum {
    THEME_GREEN(0, "green"),
    THEME_ORANGE(1, "orange"),
    THEME_RED(2, "red"),
    THEME_YELLOW(3, "yellow"),
    THEME_WATHET(4, "wathet");
    ;

    private Integer code;
    private String theme;
}
