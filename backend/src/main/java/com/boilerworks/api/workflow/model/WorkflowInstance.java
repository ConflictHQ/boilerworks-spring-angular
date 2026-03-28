package com.boilerworks.api.workflow.model;

import com.boilerworks.api.model.AuditableEntity;
import jakarta.persistence.*;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "workflow_instances")
@Getter
@Setter
@NoArgsConstructor
public class WorkflowInstance extends AuditableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workflow_definition_id", nullable = false)
  private WorkflowDefinition workflowDefinition;

  @Column(name = "current_state", nullable = false)
  private String currentState;

  @Column(name = "entity_type")
  private String entityType;

  @Column(name = "entity_id")
  private String entityId;

  @Column(name = "is_complete", nullable = false)
  private boolean complete = false;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "context_data", columnDefinition = "jsonb")
  private Map<String, Object> contextData;
}
