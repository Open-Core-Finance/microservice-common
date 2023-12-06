package tech.corefinance.common.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TraceIdContext {

    private static final TraceIdContext INSTANCE = new TraceIdContext();

    private ThreadLocal<String> traceIdThreadLocal = new InheritableThreadLocal<>();

    private TraceIdContext() {
        // Singleton
        log.debug("Created TraceIdContext [{}]", this);
    }

    public static TraceIdContext getInstance() {
        return INSTANCE;
    }

    public void setTraceId(String traceId) {
        traceIdThreadLocal.set(traceId);
    }

    public String getTraceId() {
        return traceIdThreadLocal.get();
    }

    public void clearTraceId() {
        traceIdThreadLocal.remove();
    }

}
