package com.boilerworks.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateItemRequest {

  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be at most 255 characters")
  private String name;

  @Size(max = 500, message = "Description must be at most 500 characters")
  private String description;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.00", message = "Price must be non-negative")
  private BigDecimal price;

  @NotBlank(message = "SKU is required")
  @Size(max = 100, message = "SKU must be at most 100 characters")
  private String sku;

  private boolean active = true;

  private UUID categoryId;
}
