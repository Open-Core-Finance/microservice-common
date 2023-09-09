package com.finance.core.common.converter;

public interface ExportTypeConverter<I, O> {

    O convert(I data);

    boolean isSupport(Class<?> dataType);

}
