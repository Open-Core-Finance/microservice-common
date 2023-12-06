package tech.corefinance.common.converter.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "zoned-date-time-format")
@Slf4j
public class ZonedDateTimeSerializer extends TemporalAccessorJsonSerializer<ZonedDateTime> {

    public ZonedDateTimeSerializer(
            @Value("${spring.jackson-custom.serialization.zoned-date-time-format}") String zonedDateTimeFormat) {
        super(zonedDateTimeFormat);
        log.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }
}
