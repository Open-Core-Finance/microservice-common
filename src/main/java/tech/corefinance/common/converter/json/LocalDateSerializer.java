package tech.corefinance.common.converter.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-date-format")
@Slf4j
public class LocalDateSerializer extends TemporalAccessorJsonSerializer<LocalDate> {

    public LocalDateSerializer(
            @Value("${spring.jackson-custom.serialization.local-date-format}") String localDateFormat) {
        super(localDateFormat);
        log.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

}