package com.finance.core.common.converter;

import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "kbtg.app.mongodb.converter.localDateTime", havingValue = "true", matchIfMissing = true)
@WritingConverter
public class ChronoLocalDateTimeToStringConverter implements MongoConversionSupport<ChronoLocalDateTime<?>, String>,
        GenericConverter {

    @Override
    public String convert(@Nullable ChronoLocalDateTime<?> date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(ChronoLocalDateTime.class, String.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((ChronoLocalDateTime<?>) source);
    }
}
