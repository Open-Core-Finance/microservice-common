package tech.corefinance.common.converter.json;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.providers.SpringDocProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-date-time-format")
@Slf4j
public class LocalDateTimeSerializer extends TemporalAccessorJsonSerializer<LocalDateTime> {

    public LocalDateTimeSerializer(@Value("${spring.jackson-custom.serialization.local-date-time-format}") String localDateFormat,
                                   @Autowired JsonMapper jsonMapper,
                                   @Autowired SpringDocProviders springDocProviders) {
        super(localDateFormat, jsonMapper, springDocProviders);
        log.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

}