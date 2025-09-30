package org.dows.router.handler.event;

import lombok.Data;

@Data
public class DispatchResult {
    private boolean success;
    private String message;
    private Object data;

    public DispatchResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}