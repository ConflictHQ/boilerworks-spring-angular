package com.boilerworks.api.workflow.model;

import com.boilerworks.api.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.util.Map;

/**
 * Defines a workflow state machine.
 * Schema format:
 * {
 *   "states": ["draft", "review", "approved", "rejected"],
 *   "initial": "draft",
 *   "terminal": ["approved", "rejected"],
 *   "transitions": [
 *     { "name": "submit", "from": "draft", "to": "review", "requiredPermission": "workflow.submit" },
 *     { "name": "approve", "from": "review", "to": "approved", "requiredPermission": "workflow.approve" },
 *     { "name": "reject", "from": "review", "to": "rejected", "requiredPermission": "workflow.reject" },
 *     { "name": "revise", "from": "rejected", "to": "draft" }
 *   ]
 * }
 */
@Entity
@Table(name = "workflow_definitions")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class WorkflowDefinition extends AuditableEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int version = 1;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "state_machine", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> stateMachine;
}
