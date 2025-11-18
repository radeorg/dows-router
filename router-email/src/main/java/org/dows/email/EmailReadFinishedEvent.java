package org.dows.email;

import lombok.Data;
import org.dows.router.core.RouterEvent;

@Data
public class EmailReadFinishedEvent implements RouterEvent {

    private String source;
    private String eventName;
    private Object eventData;
}
