package com.finance.core.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    @NotEmpty(message = "subjectId_must_not_null")
    private String subjectId;
    @NotEmpty(message = "subjectName_must_not_null")
    private String subjectName;
    @Min(value = 1, message = "classSize must be greater than 0")
    private int credits;

}
