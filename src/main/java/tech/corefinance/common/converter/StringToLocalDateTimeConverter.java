package tech.corefinance.common.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "tech.corefinance.app.converter.local-date-time-to-string", havingValue = "true", matchIfMissing = true)
@ReadingConverter
public class StringToLocalDateTimeConverter implements GenericConverter, CommonCustomConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@Nullable String document) {
        if (document == null) {
            return null;
        }

        return LocalDateTime.parse(document, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new GenericConverter.ConvertiblePair(String.class, LocalDateTime.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((String) source);
    }
}
