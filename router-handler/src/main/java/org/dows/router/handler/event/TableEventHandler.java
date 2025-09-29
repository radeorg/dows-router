package org.dows.router.handler.event;

import cn.hutool.json.JSONObject;
import org.dows.router.core.Routable;
import org.dows.router.core.RouterFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface TableEventHandler<T> {

    String getTableName();


    default void handle(String op, JSONObject before, JSONObject after){
        if(op.equals("c")){
            insertHandle(op, before, after);
        } else if(op.equals("u")){
            updateHandle(op, before, after);
        } else if(op.equals("d")){
            deleteHandle(op, before, after);
        } else if(op.equals("q")){
            queryHandle(op, before, after);
        }
    };

    default void insertHandle(String op, JSONObject before, JSONObject after) {
    }

    ;

    default void updateHandle(String op, JSONObject before, JSONObject after) {
    }

    ;

    default void deleteHandle(String op, JSONObject before, JSONObject after) {
    }

    ;

    default void queryHandle(String op, JSONObject before, JSONObject after) {
    }

    ;


    default void dispatch(Integer sessionType,String op,T beforeEntity,T afterEntity) {
        // sms,email,llm
        Routable router = RouterFactory.getRouter(sessionType);
        assert router != null;
        router.dispatch(op, beforeEntity, afterEntity);
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