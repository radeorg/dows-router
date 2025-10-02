package org.dows.router.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "router")
public class RouterTriggerProperties {
    private Map<String, List<TriggerItem>> trigger;

    public record TriggerItem(String source, String event, String target, String protocol) {
    }
}
