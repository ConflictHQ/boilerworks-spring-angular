package com.boilerworks.api.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransitionRequest {

    @NotBlank(message = "Transition name is required")
    private String transition;

    private String comment;
}
