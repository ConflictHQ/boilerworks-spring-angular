package com.boilerworks.api.workflow.repository;

import com.boilerworks.api.workflow.model.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, UUID> {
    List<WorkflowInstance> findByWorkflowDefinitionId(UUID definitionId);
    List<WorkflowInstance> findByEntityTypeAndEntityId(String entityType, String entityId);
}
