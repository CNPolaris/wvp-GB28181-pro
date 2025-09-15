package com.genersoft.iot.vmp.alert.feishu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 飞书消息模型接口
 */
public interface FeishuMessage {
    /**
     * 将消息转换为JSON字符串
     * @return JSON字符串
     */
    default String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("消息转换为JSON失败", e);
        }
    }
}