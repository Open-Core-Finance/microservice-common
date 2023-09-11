package tech.corefinance.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TraceIdContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final TraceIdContext INSTANCE = new TraceIdContext();

    private ThreadLocal<String> traceIdThreadLocal = new InheritableThreadLocal<>();

    private TraceIdContext() {
        // Singleton
        logger.debug("Created TraceIdContext [{}]", this);
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
