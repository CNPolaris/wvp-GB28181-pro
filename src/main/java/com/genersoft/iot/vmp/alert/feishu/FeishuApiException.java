package com.genersoft.iot.vmp.alert.feishu;

public class FeishuApiException extends RuntimeException {

    private int code;

    public FeishuApiException(String message) {
        super(message);
    }

    public FeishuApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeishuApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
