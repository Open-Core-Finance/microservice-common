package com.finance.core.common.converter.json;

import io.swagger.v3.oas.models.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@ToString
public class TemporalAccessorSchema<T extends TemporalAccessor> extends Schema<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DateTimeFormatter dateTimeFormatter;
    private Class<T> handleClass;

    public TemporalAccessorSchema(DateTimeFormatter dateTimeFormatter, Class<T> handleClass,
                                  String dateTimeFormat) {
        super("string", "object");
        this.dateTimeFormatter = dateTimeFormatter;
        this.handleClass = handleClass;
        var currentDateAsString = dateTimeFormatter.format(ZonedDateTime.now());
        logger.debug("Current date formatted [{}]", currentDateAsString);
        setPattern(dateTimeFormat);
        setExample(currentDateAsString);
    }

    @Override
    public TemporalAccessorSchema<T> type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public TemporalAccessorSchema<T> format(String format) {
        super.setFormat(format);
        return this;
    }

    @Override
    protected T cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof String) {
                    value = dateTimeFormatter.parse((String) value);
                }

                if (handleClass.isAssignableFrom(value.getClass())) {
                    return (T) value;
                }
            } catch (Exception var3) {
                logger.debug("Error", var3);
            }
        }
        return null;
    }

    @Override
    public TemporalAccessorSchema<T> _enum(List<T> _enum) {
        super.setEnum(_enum);
        return this;
    }


}
