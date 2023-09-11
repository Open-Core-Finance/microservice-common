package tech.corefinance.common.converter.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-time-format")
public class LocalTimeSerializer extends TemporalAccessorJsonSerializer<LocalTime> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LocalTimeSerializer(
            @Value("${spring.jackson-custom.serialization.local-time-format}") String localTimeFormat) {
        super(localTimeFormat);
        logger.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }

}
