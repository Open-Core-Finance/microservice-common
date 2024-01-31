package tech.corefinance.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import tech.corefinance.common.dto.JwtTokenDto;

import java.io.Serializable;
import java.util.Map;

@ConditionalOnProperty(prefix = "tech.corefinance.security.jwt.enabled", name = "common-parser", matchIfMissing = true,
        havingValue = "true")
@Slf4j
@Component
public class JwtTokenParserImpl implements JwtTokenParser {

    private ObjectMapper objectMapper;

    public JwtTokenParserImpl(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JwtTokenDto parse(String json) throws JsonProcessingException {
        var mapTypeReference = new TypeReference<Map<String, Serializable>>() {
        };
        var map = objectMapper.readValue(json, mapTypeReference);
        var resultClass = JwtTokenDto.class;
        var result = objectMapper.readValue(json, resultClass);
        for (Map.Entry<String, Serializable> entry : map.entrySet()) {
            var fieldName = entry.getKey();
            if (ReflectionUtils.findField(resultClass, fieldName) == null) {
                result.getAdditionalInfo().put(fieldName, entry.getValue());
            }
        }
        return result;
    }


}
