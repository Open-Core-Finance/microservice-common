package tech.corefinance.common.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "tech.corefinance.app.converter.zoned-date-time-to-date", havingValue = "true", matchIfMissing = true)
@WritingConverter
public class ZonedDateTimeToDateConverter implements
        GenericConverter, CommonCustomConverter<ZonedDateTime, Date> {

    @Override
    public Date convert(@Nullable ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return Date.from(zonedDateTime.withZoneSameLocal(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(ZonedDateTime.class, Date.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((ZonedDateTime) source);
    }
}
