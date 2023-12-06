package tech.corefinance.common.service;

import org.springframework.stereotype.Component;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;

import java.lang.reflect.Proxy;

@Component
public class TargetFieldProxyUnbox implements ProxyUnbox {

    private static final String TARGET_FIELD_NAME = "target";

    @Override
    public Object unProxy(Object proxy) {
        var handler = Proxy.getInvocationHandler(proxy);
        try {
            var targetField = handler.getClass().getDeclaredField(TARGET_FIELD_NAME);
            targetField.setAccessible(true);
            return targetField.get(handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectiveIncorrectFieldException(e);
        }
    }

    @Override
    public boolean canUnbox(Object proxy) {
        if (proxy != null && Proxy.isProxyClass(proxy.getClass())) {
            var handler = Proxy.getInvocationHandler(proxy);
            try {
                return handler.getClass().getDeclaredField(TARGET_FIELD_NAME) != null;
            } catch (NoSuchFieldException e) {
                return false;
            }
        }
        return false;
    }
}
