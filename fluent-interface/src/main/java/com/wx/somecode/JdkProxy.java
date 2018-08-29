package com.wx.somecode;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinquan.huangxq
 */
public class JdkProxy implements InvocationHandler {

    private Class<?> target;

    private Class<?> object;

    private Map<String, Object> properties = new HashMap<>();

    public JdkProxy(Class<?> target) {
        this.target = target;
        this.object = getParameterType(target);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isPropertySetter(method)) {
            properties.put(method.getName(), args[0]);
            return proxy;
        } else if (isBuildMethod(method)) {
            Object obj = object.newInstance();
            setProperties(obj);
            return obj;
        }
        return null;
    }

    private boolean isPropertySetter(Method method) {
        return method.getParameters().length == 1 && target.isAssignableFrom(method.getReturnType());
    }

    private boolean isBuildMethod(Method method) {
        try {
            Method buildMethod = Builder.class.getDeclaredMethod("build");
            return method.equals(buildMethod);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("");
        }
    }

    private Class<?> getParameterType(Class<?> clzz) {
        Type[] types = clzz.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getRawType() == Builder.class) {
                    return (Class<?>) paramType.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    private void setProperties(Object obj) throws Exception {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Field field = obj.getClass().getDeclaredField(key);
            boolean isAccess = field.isAccessible();
            if (!isAccess) {
                field.setAccessible(true);
            }
            try {
                field.set(obj, value);
                field.setAccessible(true);
            } finally {
                field.setAccessible(isAccess);
            }
        }
    }
}
