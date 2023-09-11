package tech.corefinance.common.converter.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Component
@ConditionalOnProperty(prefix = "spring.jackson-custom.serialization", name = "zoned-date-time-format")
@EqualsAndHashCode(callSuper=false)
public class ZonedDateTimeJsonDeserializer extends JsonDeserializer<ZonedDateTime> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DateTimeFormatter dateTimeFormatter;

    public ZonedDateTimeJsonDeserializer( @Value("${spring.jackson-custom.serialization.zoned-date-time-format}") String dateTimeFormat,
                                          @Autowired ObjectMapper objectMapper) {
        logger.debug("Applied {} format: {}", ZonedDateTime.class.getSimpleName(), dateTimeFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        SimpleModule simpleModule = new SimpleModule(ZonedDateTime.class.getSimpleName() + "DeserializerModule",
                new Version(1, 0, 0, null, "", ""));
        simpleModule.addDeserializer(ZonedDateTime.class, this);
        objectMapper.registerModule(simpleModule);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        logger.debug("Parsing {} to {}", dateString, ZonedDateTime.class);
        try {
            return ZonedDateTime.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            logger.debug("Err for " + dateTimeFormatter, e);
            return ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

}
