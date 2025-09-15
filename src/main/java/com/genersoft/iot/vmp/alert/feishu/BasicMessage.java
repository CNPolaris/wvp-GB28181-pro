package com.genersoft.iot.vmp.alert.feishu;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasicMessage implements Serializable {
    public String timestamp;
    public String sign;
}
