package com.genersoft.iot.vmp.alert.feishu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertTypeEnum {
    DEVICE_PUBLISH_NOTICE(0, "推流鉴权"),
    DEVICE_REGISTER_NOTICE(1, "设备注册"),
    DEVICE_ONLINE_NOTICE(2, "设备上线"),
    DEVICE_OFFLINE_NOTICE(3, "设备离线")

    ;
    /**
     * 类型
     */
    private Integer code;
    /**
     * 状态名
     */
    private String desc;
}