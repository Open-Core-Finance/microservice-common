package tech.corefinance.common.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Converter to convert from Date to ZonedDateTime.
 */
@Component
@ConditionalOnProperty(name = "tech.corefinance.app.converter.zoned-date-time-to-string", havingValue = "true", matchIfMissing = true)
@ReadingConverter
public class DateToZonedDateTimeConverter implements GenericConverter, CommonCustomConverter<Date, ZonedDateTime> {

    @Override
    public ZonedDateTime convert(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<GenericConverter.ConvertiblePair>();
        result.add(new GenericConverter.ConvertiblePair(Date.class, ZonedDateTime.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((Date) source);
    }
}
