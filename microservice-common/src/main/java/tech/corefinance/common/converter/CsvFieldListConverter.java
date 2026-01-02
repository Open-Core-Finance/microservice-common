package tech.corefinance.common.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import tech.corefinance.common.dto.CsvExportDefinition;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;
import java.util.Objects;

@Component
@Slf4j
public class CsvFieldListConverter implements CommonCustomConverter<String, CsvExportDefinition[]>, Formatter<CsvExportDefinition[]> {

    private final JsonMapper jsonMapper = new JsonMapper();

    @Override
    public CsvExportDefinition[] convert(@NonNull String value) {
        return jsonMapper.readValue(value, CsvExportDefinition[].class);
    }

    @Override
    @NonNull
    public String print(@Nullable CsvExportDefinition[] object, @Nullable Locale locale) {
        log.info("Calling custom formatter for CsvExportDefinition[] {}! Locale ignored!", (Object) object);
        return jsonMapper.writeValueAsString(object);
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
