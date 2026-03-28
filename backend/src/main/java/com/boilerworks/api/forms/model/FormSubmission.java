package com.boilerworks.api.forms.model;

import com.boilerworks.api.model.AuditableEntity;
import jakarta.persistence.*;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "form_submissions")
@Getter
@Setter
@NoArgsConstructor
public class FormSubmission extends AuditableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "form_definition_id", nullable = false)
  private FormDefinition formDefinition;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "data", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> data;

  @Column(name = "is_valid", nullable = false)
  private boolean valid = true;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "validation_errors", columnDefinition = "jsonb")
  private Map<String, Object> validationErrors;
}
