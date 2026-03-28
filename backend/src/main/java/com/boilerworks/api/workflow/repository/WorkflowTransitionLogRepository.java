package com.boilerworks.api.workflow.repository;

import com.boilerworks.api.workflow.model.WorkflowTransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowTransitionLogRepository extends JpaRepository<WorkflowTransitionLog, UUID> {
    List<WorkflowTransitionLog> findByWorkflowInstanceIdOrderByPerformedAtAsc(UUID instanceId);
}
