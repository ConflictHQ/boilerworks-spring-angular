package com.boilerworks.api.workflow.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "workflow_transition_logs")
@Getter
@Setter
@NoArgsConstructor
public class WorkflowTransitionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workflow_instance_id", nullable = false)
  private WorkflowInstance workflowInstance;

  @Column(name = "transition_name", nullable = false)
  private String transitionName;

  @Column(name = "from_state", nullable = false)
  private String fromState;

  @Column(name = "to_state", nullable = false)
  private String toState;

  @Column(name = "performed_by")
  private UUID performedBy;

  @Column(name = "performed_at", nullable = false)
  private Instant performedAt;

  @Column(columnDefinition = "TEXT")
  private String comment;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "metadata", columnDefinition = "jsonb")
  private Map<String, Object> metadata;
}
