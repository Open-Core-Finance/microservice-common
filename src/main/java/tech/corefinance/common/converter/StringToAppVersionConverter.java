package tech.corefinance.common.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import tech.corefinance.common.model.AppVersion;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class StringToAppVersionConverter implements CommonCustomConverter<String, AppVersion>, GenericConverter {

    @Override
    public AppVersion convert(String source) {
        if (source != null) {
            source = source.trim();
            return new AppVersion(source);
        }
        return null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new GenericConverter.ConvertiblePair(String.class, AppVersion.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((String) source);
    }
}
