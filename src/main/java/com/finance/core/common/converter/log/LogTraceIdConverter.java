package com.finance.core.common.converter.log;

import com.finance.core.common.context.TraceIdContext;

public class LogTraceIdConverter extends LogDataConverter {

    public LogTraceIdConverter() {
        super("Trace ID");
    }

    @Override
    protected String retrieveData() {
        var context = TraceIdContext.getInstance();
        if (context == null) {
            return null;
        }
        return context.getTraceId();
    }

}
