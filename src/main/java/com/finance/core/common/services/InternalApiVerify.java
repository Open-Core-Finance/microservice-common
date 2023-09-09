package com.finance.core.common.services;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

public interface InternalApiVerify {

    boolean internalPermissionCheck(Class<?> controllerClass, Method method, HttpServletRequest request);

}
