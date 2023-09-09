package com.finance.core.common.converter.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-date-format")
public class LocalDateSerializer extends TemporalAccessorJsonSerializer<LocalDate> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LocalDateSerializer(
            @Value("${spring.jackson-custom.serialization.local-date-format}") String localDateFormat) {
        super(localDateFormat);
        logger.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

}