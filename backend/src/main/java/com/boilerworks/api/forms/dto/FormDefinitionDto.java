package com.boilerworks.api.forms.dto;

import com.boilerworks.api.forms.model.FormDefinition;
import com.boilerworks.api.forms.model.FormStatus;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDefinitionDto {

  private UUID id;
  private String name;
  private String slug;
  private String description;
  private int version;
  private FormStatus status;
  private Map<String, Object> schema;
  private Map<String, Object> logicRules;
  private Instant createdAt;
  private Instant updatedAt;

  public static FormDefinitionDto from(FormDefinition form) {
    FormDefinitionDto dto = new FormDefinitionDto();
    dto.setId(form.getId());
    dto.setName(form.getName());
    dto.setSlug(form.getSlug());
    dto.setDescription(form.getDescription());
    dto.setVersion(form.getVersion());
    dto.setStatus(form.getStatus());
    dto.setSchema(form.getSchema());
    dto.setLogicRules(form.getLogicRules());
    dto.setCreatedAt(form.getCreatedAt());
    dto.setUpdatedAt(form.getUpdatedAt());
    return dto;
  }
}
