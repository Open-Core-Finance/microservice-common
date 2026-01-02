package tech.corefinance.common.service;

import tech.corefinance.common.dto.JwtTokenDto;

public interface JwtTokenParser {

    JwtTokenDto parse(String json);

}
