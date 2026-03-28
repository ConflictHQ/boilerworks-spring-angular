package com.boilerworks.api.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class CreateWorkflowRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "State machine definition is required")
    private Map<String, Object> stateMachine;
}
