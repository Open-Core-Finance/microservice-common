package tech.corefinance.common.converter.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-time-format")
@Slf4j
public class LocalTimeSerializer extends TemporalAccessorJsonSerializer<LocalTime> {

    public LocalTimeSerializer(
            @Value("${spring.jackson-custom.serialization.local-time-format}") String localTimeFormat) {
        super(localTimeFormat);
        log.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }

}
