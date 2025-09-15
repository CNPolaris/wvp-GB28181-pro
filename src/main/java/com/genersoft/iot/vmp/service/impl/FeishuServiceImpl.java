package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.alert.feishu.AlertCardThemeEnum;
import com.genersoft.iot.vmp.alert.feishu.AlertTypeEnum;
import com.genersoft.iot.vmp.alert.feishu.FeishuApiClient;
import com.genersoft.iot.vmp.service.IFeishuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeishuServiceImpl implements IFeishuService {

    private static final Logger logger = LoggerFactory.getLogger(FeishuServiceImpl.class);

    @Resource
    private FeishuApiClient feishuApiClient;
    /**
     * 发送飞书卡片通知
     *
     * @param message       通知消息内容
     * @param alertTypeEnum 通知类型枚举
     * @param cardTheme
     */
    @Override
    public void sendCardNotice(String message, AlertTypeEnum alertTypeEnum, AlertCardThemeEnum cardTheme) {
        feishuApiClient.sendCardNotice(message, alertTypeEnum, cardTheme);
    }

    /**
     * 异步发送飞书卡片通知
     *
     * @param message       要发送的通知消息内容
     * @param alertTypeEnum 通知类型枚举
     * @param cardTheme
     */
    @Override
    @Async("feishuAsyncExecutor")
    public CompletableFuture<Void> sendCardNoticeAsync(String message, AlertTypeEnum alertTypeEnum, AlertCardThemeEnum cardTheme) {
        try {
            feishuApiClient.sendCardNotice(message, alertTypeEnum, cardTheme);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("异步发送消息失败", e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
