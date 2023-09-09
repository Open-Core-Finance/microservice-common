package com.finance.core.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.core.common.model.AppVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringToAppVersionConverter implements Converter<String, AppVersion>, GenericConverter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AppVersion convert(String source) {
        if (source != null) {
            source = source.trim();
            var build = "";
            var lastIndex = source.lastIndexOf("-");
            if (lastIndex > 0) {
                build = source.substring(lastIndex + 1);
                source = source.substring(0, lastIndex);
            } else if (source.contains(".")) {
                lastIndex = source.lastIndexOf(".");
                build = source.substring(lastIndex + 1);
                source = source.substring(0, lastIndex);
            }
            if (source.contains(".")) {
                String[] values = source.split("\\.");
                var result = new AppVersion(Short.valueOf(values[0]), Short.valueOf(values[1]), Short.valueOf(values[2]), build);
                logger.info("Converting app version value {} to object {}", source, result);
                return result;
            } else {
                try {
                    return objectMapper.readValue(source, AppVersion.class);
                } catch (IOException e) {
                    logger.error("Error", e);
                    return null;
                }
            }
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
