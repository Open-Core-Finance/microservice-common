package tech.corefinance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleVersion {
    private short major;
    private short minor;
}
