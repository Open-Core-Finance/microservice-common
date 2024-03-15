package tech.corefinance.product.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.product.entity.ExchangeRateId;

@Component
@RequiredArgsConstructor
public class StringToExchangeRateId implements Converter<String, ExchangeRateId> {

    private final ObjectMapper objectMapper;

    @Override
    public ExchangeRateId convert(String source) {
        try {
            return objectMapper.readValue(source, ExchangeRateId.class);
        } catch (JsonProcessingException e) {
            throw new ServiceProcessingException(e.getMessage(), e);
        }
    }
}
