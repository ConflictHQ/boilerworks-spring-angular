package com.boilerworks.api.forms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class SubmitFormRequest {

    @NotNull(message = "Form data is required")
    private Map<String, Object> data;
}
