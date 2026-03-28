package com.boilerworks.api.forms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class CreateFormRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Schema is required")
    private Map<String, Object> schema;

    private Map<String, Object> logicRules;
}
