package com.boilerworks.api.dto;

import com.boilerworks.api.model.Item;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

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

  public static ItemDto from(Item item) {
    ItemDto dto = new ItemDto();
    dto.setId(item.getId());
    dto.setName(item.getName());
    dto.setSlug(item.getSlug());
    dto.setDescription(item.getDescription());
    dto.setPrice(item.getPrice());
    dto.setSku(item.getSku());
    dto.setActive(item.isActive());
    if (item.getCategory() != null) {
      dto.setCategoryId(item.getCategory().getId());
      dto.setCategoryName(item.getCategory().getName());
    }
    dto.setCreatedAt(item.getCreatedAt());
    dto.setUpdatedAt(item.getUpdatedAt());
    return dto;
  }
}
