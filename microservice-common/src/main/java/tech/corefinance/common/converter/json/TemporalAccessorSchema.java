package tech.corefinance.common.converter.json;

import io.swagger.v3.oas.models.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@ToString
@Slf4j
public class TemporalAccessorSchema<T extends TemporalAccessor> extends Schema<T> {

    private DateTimeFormatter dateTimeFormatter;
    private Class<T> handleClass;

    public TemporalAccessorSchema(DateTimeFormatter dateTimeFormatter, Class<T> handleClass,
                                  String dateTimeFormat) {
        super("string", "object");
        this.dateTimeFormatter = dateTimeFormatter;
        this.handleClass = handleClass;
        var currentDateAsString = dateTimeFormatter.format(ZonedDateTime.now());
        log.debug("Current date formatted [{}]", currentDateAsString);
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
                log.debug("Error", var3);
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
