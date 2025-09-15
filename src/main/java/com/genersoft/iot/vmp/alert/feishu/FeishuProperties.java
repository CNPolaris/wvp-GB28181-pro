package com.genersoft.iot.vmp.alert.feishu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 飞书配置属性
 */
@Component
@ConfigurationProperties(prefix = "feishu")
public class FeishuProperties {

    private Robot robot = new Robot();

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public static class Robot {
        private String webhookUrl;
        private String sign;
        // 连接超时时间，单位毫秒
        private int connectTimeout = 5000;

        // 读取超时时间，单位毫秒
        private int readTimeout = 5000;

        public String getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }
        public String getSign() {
            return sign;
        }
        public void setSign(String sign) {
            this.sign = sign;
        }
        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }
    }
}
