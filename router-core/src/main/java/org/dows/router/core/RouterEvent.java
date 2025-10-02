package org.dows.router.core;

public interface RouterEvent {

    String getSource();

    String getEventName();

    Object getEventData();
}
