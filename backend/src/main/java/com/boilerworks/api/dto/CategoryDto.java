package com.boilerworks.api.dto;

import com.boilerworks.api.model.Category;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

  private UUID id;
  private String name;
  private String slug;
  private String description;
  private int sortOrder;
  private Instant createdAt;
  private Instant updatedAt;

  public static CategoryDto from(Category category) {
    CategoryDto dto = new CategoryDto();
    dto.setId(category.getId());
    dto.setName(category.getName());
    dto.setSlug(category.getSlug());
    dto.setDescription(category.getDescription());
    dto.setSortOrder(category.getSortOrder());
    dto.setCreatedAt(category.getCreatedAt());
    dto.setUpdatedAt(category.getUpdatedAt());
    return dto;
  }
}
