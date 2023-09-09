package com.finance.core.common.converter.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "zoned-date-time-format")
public class ZonedDateTimeSerializer extends TemporalAccessorJsonSerializer<ZonedDateTime> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ZonedDateTimeSerializer(
            @Value("${spring.jackson-custom.serialization.zoned-date-time-format}") String zonedDateTimeFormat) {
        super(zonedDateTimeFormat);
        logger.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }
}
