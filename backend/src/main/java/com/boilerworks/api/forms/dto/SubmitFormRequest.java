package com.boilerworks.api.forms.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

@Data
public class SubmitFormRequest {

  @NotNull(message = "Form data is required")
  private Map<String, Object> data;
}
