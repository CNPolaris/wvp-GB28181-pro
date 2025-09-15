package com.genersoft.iot.vmp.alert.feishu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.alibaba.fastjson.JSONObject;
import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
/**
 * 飞书API客户端，封装飞书Webhook调用细节
 */
@Component
public class FeishuApiClient {

    private static final Logger logger = LoggerFactory.getLogger(FeishuApiClient.class);

    @Resource
    private FeishuProperties feishuProperties;

    @Resource
    private RestTemplate feishuRestTemplate;

    /**
     * 使用HmacSHA256算法生成签名
     * @param secret 用于生成签名的密钥
     * @param timestamp 时间戳，与密钥一起组成签名字符串
     * @return Base64编码后的签名字符串
     * @throws NoSuchAlgorithmException 如果HmacSHA256算法不可用
     * @throws InvalidKeyException 如果提供的密钥无效
     */
    private static String GenSign(String secret, int timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.encodeBase64(signData));
    }

    /**
     * 构建卡片告警请求对象
     * @param alertText 告警文本内容
     * @param alertType 告警类型枚举
     * @param isAt 是否@相关人员
     * @return 构建完成的卡片告警请求对象
     */
    protected CardAlertRequest buildCardRequest(String alertText, AlertTypeEnum alertType, AlertCardThemeEnum cardTheme, Boolean isAt) {
        List<CardAlertRequest.Element> elements = new ArrayList<>();
        elements.add(buildCardText(alertText, alertType, isAt));

        CardAlertRequest.Card card = new CardAlertRequest.Card(elements, buildCardHeader(alertType, cardTheme));
        return CardAlertRequest.builder().card(card).msg_type("interactive").build();
    }

    /**
     * 构建卡片文本元素
     * @param alertText 告警文本内容
     * @param alertType 告警类型枚举
     * @param isAt 是否@相关人员
     * @return 构建完成的卡片文本元素对象
     */
    protected CardAlertRequest.Element buildCardText(String alertText, AlertTypeEnum alertType, Boolean isAt) {
        StringBuilder builder = new StringBuilder();
        builder.append(alertText);

        JSONObject text = new JSONObject();
        text.put("content", builder.toString());
        text.put("tag", "lark_md");
        return CardAlertRequest.Element.builder().tag("div").text(text).build();
    }

    /**
     * 构建卡片告警请求的头部信息
     * @param alertType 告警类型枚举，不能为null
     * @return 构建完成的卡片告警请求头部对象，包含标题和副标题
     */
    protected CardAlertRequest.Header buildCardHeader(@NotNull AlertTypeEnum alertType, AlertCardThemeEnum cardTheme) {
        String content = MessageFormat.format("【网联中心】{0}", alertType.getDesc());
        CardAlertRequest.Title title = CardAlertRequest.Title.builder().tag("plain_text")
                .content(content)
                .build();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String subContent = MessageFormat.format("通知时间: {0}", now.format(dateTimeFormatter));
        CardAlertRequest.SubTitle subTitle = CardAlertRequest.SubTitle.builder().tag("plain_text").content(subContent).build();
        return CardAlertRequest.Header.builder().template(cardTheme.getTheme()).title(title).subTitle(subTitle).build();
    }

    /**
     * 发送飞书卡片通知消息
     * @param message 要发送的消息内容 JSON格式
     * @param alertType 消息类型，参考AlertTypeEnum枚举
     * @throws RuntimeException 当签名生成失败时抛出
     */
    public void sendCardNotice(String message, AlertTypeEnum alertType, AlertCardThemeEnum cardTheme) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        String sign;
        try {
            sign = GenSign(feishuProperties.getRobot().getSign(), timestamp);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        // 卡片消息体
        CardAlertRequest cardAlertRequest = buildCardRequest(
                message,
                alertType,
                cardTheme,
                false
        );
        // 校验码与时间戳
        cardAlertRequest.setTimestamp(String.valueOf(timestamp));
        cardAlertRequest.setSign(sign);
        logger.info("Request Message {}", cardAlertRequest.toJson());

        send(cardAlertRequest.toJson());
    }

    /**
     * 通过飞书机器人发送消息到指定webhook
     * @param message 要发送的消息内容（JSON格式字符串）
     * @throws FeishuApiException 当消息发送失败时抛出，包含HTTP状态码和错误响应
     */
    private void send(String message) {
        String webhookUrl = feishuProperties.getRobot().getWebhookUrl();
        // 消息头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(message, headers);
        ResponseEntity<String> response = feishuRestTemplate.postForEntity(webhookUrl, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("飞书消息发送失败，状态码: {}, 响应: {}", response.getStatusCode(), response.getBody());
            throw new FeishuApiException(response.getStatusCode().value(), "飞书消息发送失败: " + response.getBody());
        }
        logger.info("飞书消息发送成功，响应: {}", response.getBody());
    }
}
