package com.finance.core.common.converter;

import org.springframework.core.convert.converter.Converter;

public interface MongoConversionSupport<F, T> extends Converter<F, T> {
}