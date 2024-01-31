package tech.corefinance.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.corefinance.common.dto.JwtTokenDto;

public interface JwtTokenParser {

    JwtTokenDto parse(String json) throws JsonProcessingException;

}
