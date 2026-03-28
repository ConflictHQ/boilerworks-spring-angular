package com.boilerworks.api.dto;

import com.boilerworks.api.model.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private UUID id;
  private String name;
  private String slug;
  private String description;
  private BigDecimal price;
  private String sku;
  private boolean active;
  private UUID categoryId;
  private String categoryName;
  private Instant createdAt;
  private Instant updatedAt;

  public static ProductDto from(Product product) {
    ProductDto dto = new ProductDto();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setSlug(product.getSlug());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setSku(product.getSku());
    dto.setActive(product.isActive());
    if (product.getCategory() != null) {
      dto.setCategoryId(product.getCategory().getId());
      dto.setCategoryName(product.getCategory().getName());
    }
    dto.setCreatedAt(product.getCreatedAt());
    dto.setUpdatedAt(product.getUpdatedAt());
    return dto;
  }
}
