package tech.corefinance.common.service;

import java.lang.reflect.Proxy;

public interface ProxyUnbox {

    Object unProxy(Object proxy);

    default boolean canUnbox(Object proxy) {
        return proxy!= null && Proxy.isProxyClass(proxy.getClass());
    }
}
