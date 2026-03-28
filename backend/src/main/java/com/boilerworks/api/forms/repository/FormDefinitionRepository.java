package com.boilerworks.api.forms.repository;

import com.boilerworks.api.forms.model.FormDefinition;
import com.boilerworks.api.forms.model.FormStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormDefinitionRepository extends JpaRepository<FormDefinition, UUID> {
    Optional<FormDefinition> findBySlug(String slug);
    List<FormDefinition> findByStatus(FormStatus status);
}
