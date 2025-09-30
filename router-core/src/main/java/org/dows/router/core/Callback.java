package org.dows.router.core;

public interface Callback {

    default void onSuccess(Object data) {

    }

    default void onFailure(String message) {

    }

}

