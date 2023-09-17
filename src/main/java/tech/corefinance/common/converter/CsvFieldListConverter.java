package tech.corefinance.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import tech.corefinance.common.dto.CsvExportDefinition;

import java.util.Locale;

@Component
@Slf4j
public class CsvFieldListConverter implements CommonCustomConverter<String, CsvExportDefinition[]>,
        Formatter<CsvExportDefinition[]> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows(JsonProcessingException.class)
    @Override
    public CsvExportDefinition[] convert(String value) {
        return objectMapper.readValue(value, CsvExportDefinition[].class);
    }

    @SneakyThrows(JsonProcessingException.class)
    @Override
    public String print(CsvExportDefinition[] object, Locale locale) {
        log.info("Calling custom formatter for CsvExportDefinition[] {}! Locale ignored!", (Object) object);
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public CsvExportDefinition[] parse(String text, Locale locale) {
        log.info("Calling custom formatter to parse {] to CsvExportDefinition[]! Locale ignored!", text);
        return convert(text);
    }

}
