package tech.corefinance.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import tech.corefinance.common.dto.CsvExportDefinition;

import java.util.Locale;
import java.util.Objects;

@Component
@Slf4j
public class CsvFieldListConverter implements CommonCustomConverter<String, CsvExportDefinition[]>, Formatter<CsvExportDefinition[]> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows(JsonProcessingException.class)
    @Override
    public CsvExportDefinition[] convert(@NonNull String value) {
        return objectMapper.readValue(value, CsvExportDefinition[].class);
    }

    @SneakyThrows(JsonProcessingException.class)
    @Override
    @NonNull
    public String print(@Nullable CsvExportDefinition[] object, @Nullable Locale locale) {
        log.info("Calling custom formatter for CsvExportDefinition[] {}! Locale ignored!", (Object) object);
        return objectMapper.writeValueAsString(object);
    }

    @Override
    @NonNull
    public CsvExportDefinition[] parse(@Nullable String text, @Nullable Locale locale) {
        log.info("Calling custom formatter to parse {} to CsvExportDefinition[]! Locale ignored!", text);
        if (text == null) {
            throw new IllegalArgumentException();
        }
        return Objects.requireNonNull(convert(text));
    }

}
