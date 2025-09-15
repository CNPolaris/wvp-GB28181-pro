package com.genersoft.iot.vmp.service;

import com.genersoft.iot.vmp.alert.feishu.AlertCardThemeEnum;
import com.genersoft.iot.vmp.alert.feishu.AlertTypeEnum;

import java.util.concurrent.CompletableFuture;

public interface IFeishuService {
    /**
     * 发送飞书卡片通知
     * @param message 通知消息内容
     * @param alertTypeEnum 通知类型枚举
     */
    void sendCardNotice(String message, AlertTypeEnum alertTypeEnum, AlertCardThemeEnum cardTheme);

    /**
     * 异步发送飞书卡片通知
     * @param message 要发送的通知消息内容
     * @param alertTypeEnum 通知类型枚举
     */
    CompletableFuture<Void> sendCardNoticeAsync(String message, AlertTypeEnum alertTypeEnum, AlertCardThemeEnum cardTheme);

}
