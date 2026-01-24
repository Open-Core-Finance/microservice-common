package tech.corefinance.common.converter.json;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.Version;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "zoned-date-time-format")
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class ZonedDateTimeJsonDeserializer extends ValueDeserializer<ZonedDateTime> {

    private DateTimeFormatter dateTimeFormatter;

    public ZonedDateTimeJsonDeserializer(@Value("${spring.jackson-custom.serialization.zoned-date-time-format}") String dateTimeFormat,
                                         @Autowired JsonMapper jsonMapper) {
        log.debug("Applied {} format: {}", ZonedDateTime.class.getSimpleName(), dateTimeFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

        Version version = new Version(1, 0, 0, null, "", "");
        SimpleModule simpleModule = new SimpleModule(ZonedDateTime.class.getSimpleName() + "DeserializerModule", version);
        simpleModule.addDeserializer(ZonedDateTime.class, this);
        jsonMapper.registeredModules().add(simpleModule);
    }

    @Override
    public ZonedDateTime deserialize(tools.jackson.core.JsonParser p, tools.jackson.databind.DeserializationContext ctxt)
            throws JacksonException {
        String dateString = p.getString();
        log.debug("Parsing {} to {}", dateString, ZonedDateTime.class);
        try {
            return ZonedDateTime.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            log.debug("Err for {}", dateTimeFormatter, e);
            return ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

}
