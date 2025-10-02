package org.dows.router.core;

import java.util.EventObject;

public interface Callback {

    default void onSuccess(RouterEvent routerEvent) {

    }

    default void onFailure(RouterEvent routerEvent) {

    }

}

