package org.dows.router.handler.event;

import cn.hutool.json.JSONObject;
import org.dows.router.core.Callback;
import org.dows.router.core.Routable;
import org.dows.router.core.RouterFactory;
import org.dows.router.core.ThreadPoolHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface TableEventHandler<T> {

    String getTableName();


    default void handle(String op, JSONObject before, JSONObject after) {
        T beforeData = convert(before);
        T afterData = convert(after);
        switch (op) {
            case "c" -> insertHandle(op, beforeData, afterData);
            case "u" -> updateHandle(op, beforeData, afterData);
            case "d" -> deleteHandle(op, beforeData, afterData);
            case "q" -> queryHandle(op, beforeData, afterData);
        }
    }

    ;

    default void insertHandle(String op, T before, T after) {
    }

    ;

    default void updateHandle(String op, T before, T after) {
    }

    ;

    default void deleteHandle(String op, T before, T after) {
    }

    ;

    default void queryHandle(String op, T before, T after) {
    }

    ;


    default void dispatch(Integer sessionType, String op, T beforeEntity, T afterEntity, Callback callback) {
        // sms,email,llm
        Routable<T> router = RouterFactory.getRouter(sessionType);
        ThreadPoolHolder.getRouterExecutorService().submit(() -> {
            try {
                // 调用路由处理逻辑,各种情况通过callback进行回调处理
                router.dispatch(op, beforeEntity, afterEntity, callback);
            } catch (Exception e) {
                // 异常处理，可根据实际需求调整
                e.printStackTrace();
            }
        });
    }

    default T convert(JSONObject jsonObject) {
        // 获取实际的泛型类型参数
        Type genericInterface = null;
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();

        for (Type gi : genericInterfaces) {
            if (gi instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) gi;
                if (pt.getRawType() == TableEventHandler.class) {
                    genericInterface = gi;
                    break;
                }
            }
        }

        if (genericInterface == null) {
            // 如果在接口中找不到，则检查父类
            Type genericSuperclass = this.getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericSuperclass;
                if (pt.getRawType() == TableEventHandler.class) {
                    genericInterface = genericSuperclass;
                }
            }
        }

        if (genericInterface instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            return jsonObject.toBean((Class<T>) actualTypeArgument);
        } else {
            // 如果无法确定泛型类型，则回退到原来的实现方式（可能不准确）
            return jsonObject.toBean((Class<T>) getClass());
        }
    }
}