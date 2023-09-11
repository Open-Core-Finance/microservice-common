package tech.corefinance.common.converter;

import java.time.chrono.ChronoLocalDate;
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
@ConditionalOnProperty(name = "tech.corefinance.app.mongodb.converter.localDate", havingValue = "true", matchIfMissing = true)
@WritingConverter
public class ChronoLocalDateToStringConverter
        implements MongoConversionSupport<ChronoLocalDate, String>, GenericConverter {

    @Override
    public String convert(@Nullable ChronoLocalDate date) {
        if (date == null) {
            return null;
        }

        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(ChronoLocalDate.class, String.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((ChronoLocalDate) source);
    }
}
