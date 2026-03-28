package com.boilerworks.api.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

  @Size(max = 255, message = "Name must be at most 255 characters")
  private String name;

  @Size(max = 500, message = "Description must be at most 500 characters")
  private String description;

  private Integer sortOrder;
}
