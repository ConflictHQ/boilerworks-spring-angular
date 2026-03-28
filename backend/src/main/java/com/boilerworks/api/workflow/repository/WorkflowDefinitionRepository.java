package com.boilerworks.api.workflow.repository;

import com.boilerworks.api.workflow.model.WorkflowDefinition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, UUID> {
  Optional<WorkflowDefinition> findBySlug(String slug);

  List<WorkflowDefinition> findByActiveTrue();
}
