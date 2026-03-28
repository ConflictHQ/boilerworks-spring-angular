package com.boilerworks.api.workflow.repository;

import com.boilerworks.api.workflow.model.WorkflowTransitionLog;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTransitionLogRepository
    extends JpaRepository<WorkflowTransitionLog, UUID> {
  List<WorkflowTransitionLog> findByWorkflowInstanceIdOrderByPerformedAtAsc(UUID instanceId);
}
