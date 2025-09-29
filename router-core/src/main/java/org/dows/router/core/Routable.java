package org.dows.router.core;

public interface Routable<T> {
    default void dispatch(String op, T beforeEntity, T afterEntity) {
    }

    ;
}
