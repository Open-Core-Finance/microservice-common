package com.finance.core.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleVersion {
    private short major;
    private short minor;
}
