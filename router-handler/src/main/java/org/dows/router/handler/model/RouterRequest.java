package org.dows.router.handler.model;

import lombok.Data;

/**
 * 路由请求模型
 */
@Data
public class RouterRequest {
    private String rawData;
    private String appId;
    private Long operatorId;
    private Long routerChannelId;
    private Integer sessionType;
    private Integer priority;

    // 构造函数
    public RouterRequest() {
    }

    public RouterRequest(String rawData, String appId, Long operatorId,
                         Long routerChannelId, Integer sessionType) {
        this(rawData, appId, operatorId, routerChannelId, sessionType, 0);
    }

    public RouterRequest(String rawData, String appId, Long operatorId,
                         Long routerChannelId, Integer sessionType, Integer priority) {
        this.rawData = rawData;
        this.appId = appId;
        this.operatorId = operatorId;
        this.routerChannelId = routerChannelId;
        this.sessionType = sessionType;
        this.priority = priority;
    }

}