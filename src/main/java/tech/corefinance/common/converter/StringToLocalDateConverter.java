package tech.corefinance.common.converter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "tech.corefinance.app.converter.local-date-to-string", havingValue = "true", matchIfMissing = true)
@ReadingConverter
public class StringToLocalDateConverter implements GenericConverter, CommonCustomConverter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String document) {
        if (document == null) {
            return null;
        }

        return LocalDate.parse(document, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<GenericConverter.ConvertiblePair>();
        result.add(new GenericConverter.ConvertiblePair(String.class, LocalDate.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((String) source);
    }
}
