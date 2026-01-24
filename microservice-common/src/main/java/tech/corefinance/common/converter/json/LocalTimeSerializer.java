package tech.corefinance.common.converter.json;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.providers.SpringDocProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalTime;

@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "local-time-format")
@Slf4j
public class LocalTimeSerializer extends TemporalAccessorJsonSerializer<LocalTime> {

    public LocalTimeSerializer(@Value("${spring.jackson-custom.serialization.local-time-format}") String localTimeFormat,
                               @Autowired JsonMapper jsonMapper,
                               @Autowired SpringDocProviders springDocProviders) {
        super(localTimeFormat, jsonMapper, springDocProviders);
        log.debug("Initialized JSON serializer for {}", handledType().getName());
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }

}
