package com.boilerworks.api.workflow.dto;

import com.boilerworks.api.workflow.model.WorkflowInstance;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstanceDto {

  private UUID id;
  private UUID workflowDefinitionId;
  private String currentState;
  private String entityType;
  private String entityId;
  private boolean complete;
  private Map<String, Object> contextData;
  private Instant createdAt;
  private Instant updatedAt;

  public static WorkflowInstanceDto from(WorkflowInstance instance) {
    WorkflowInstanceDto dto = new WorkflowInstanceDto();
    dto.setId(instance.getId());
    dto.setWorkflowDefinitionId(instance.getWorkflowDefinition().getId());
    dto.setCurrentState(instance.getCurrentState());
    dto.setEntityType(instance.getEntityType());
    dto.setEntityId(instance.getEntityId());
    dto.setComplete(instance.isComplete());
    dto.setContextData(instance.getContextData());
    dto.setCreatedAt(instance.getCreatedAt());
    dto.setUpdatedAt(instance.getUpdatedAt());
    return dto;
  }
}
