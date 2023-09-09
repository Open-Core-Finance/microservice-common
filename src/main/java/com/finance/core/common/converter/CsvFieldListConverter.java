package com.finance.core.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.core.common.dto.CsvExportDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class CsvFieldListConverter implements Converter<String, CsvExportDefinition[]>,
        Formatter<CsvExportDefinition[]> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CsvExportDefinition[] convert(String value) {
        try {
            return objectMapper.readValue(value, CsvExportDefinition[].class);
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Error when parse data []", value, e);
            return null;
        }
    }

    @Override
    public String print(CsvExportDefinition[] object, Locale locale) {
        try {
            logger.info("Calling custom formatter for CsvExportDefinition[] {}! Locale ignored!", (Object) object);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error", e);
            return "[]";
        }
    }

    @Override
    public CsvExportDefinition[] parse(String text, Locale locale) {
        logger.info("Calling custom formatter to parse {] to CsvExportDefinition[]! Locale ignored!", text);
        return convert(text);
    }

}
