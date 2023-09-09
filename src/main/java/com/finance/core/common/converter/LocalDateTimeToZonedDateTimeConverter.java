package com.finance.core.common.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "com.finance.core.app.mongodb.converter.zonedDateTime", havingValue = "true", matchIfMissing = true)
@ReadingConverter
public class LocalDateTimeToZonedDateTimeConverter implements MongoConversionSupport<LocalDateTime, ZonedDateTime>, GenericConverter {

    @Override
    public ZonedDateTime convert(@Nullable LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.of(date, ZoneId.systemDefault());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(LocalDateTime.class, ZonedDateTime.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((LocalDateTime) source);
    }
}
