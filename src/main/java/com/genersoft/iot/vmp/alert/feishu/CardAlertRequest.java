package com.genersoft.iot.vmp.alert.feishu;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAlertRequest extends BasicMessage implements FeishuMessage {
    /**
     * 消息类型
     */
    private String msg_type;
    /**
     * 消息体
     */
    private Card card;

    @Data
    @AllArgsConstructor
    public static class Card {
        /**
         * 元素
         */
        private List<Element> elements;
        /**
         * 头部
         */
        private Header header;
    }

    @Data
    @Builder
    public static class Element {
        public String tag;
        private JSONObject text;
        private List<JSONObject> actions;
    }

    @Data
    @Builder
    public static class Header {
        private String template;
        private Title title;
        private SubTitle subTitle;
    }

    @Data
    @Builder
    public static class Title {
        private String tag;
        private String content;
        private String subTitle;
    }

    @Data
    @Builder
    public static class SubTitle {
        private String tag;
        private String content;
    }
}
