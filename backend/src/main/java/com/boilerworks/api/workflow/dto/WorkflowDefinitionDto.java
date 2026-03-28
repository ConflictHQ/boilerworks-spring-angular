package com.boilerworks.api.workflow.dto;

import com.boilerworks.api.workflow.model.WorkflowDefinition;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDefinitionDto {

  private UUID id;
  private String name;
  private String slug;
  private String description;
  private int version;
  private boolean active;
  private Map<String, Object> stateMachine;
  private Instant createdAt;
  private Instant updatedAt;

  public static WorkflowDefinitionDto from(WorkflowDefinition wf) {
    WorkflowDefinitionDto dto = new WorkflowDefinitionDto();
    dto.setId(wf.getId());
    dto.setName(wf.getName());
    dto.setSlug(wf.getSlug());
    dto.setDescription(wf.getDescription());
    dto.setVersion(wf.getVersion());
    dto.setActive(wf.isActive());
    dto.setStateMachine(wf.getStateMachine());
    dto.setCreatedAt(wf.getCreatedAt());
    dto.setUpdatedAt(wf.getUpdatedAt());
    return dto;
  }
}
