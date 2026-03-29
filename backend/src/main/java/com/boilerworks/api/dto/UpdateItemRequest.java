package com.boilerworks.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateItemRequest {

  @Size(max = 255, message = "Name must be at most 255 characters")
  private String name;

  @Size(max = 500, message = "Description must be at most 500 characters")
  private String description;

  @DecimalMin(value = "0.00", message = "Price must be non-negative")
  private BigDecimal price;

  @Size(max = 100, message = "SKU must be at most 100 characters")
  private String sku;

  private Boolean active;

  private UUID categoryId;
}
