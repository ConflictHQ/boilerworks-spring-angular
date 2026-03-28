package com.boilerworks.api.forms.dto;

import com.boilerworks.api.forms.model.FormSubmission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionDto {

    private UUID id;
    private UUID formDefinitionId;
    private Map<String, Object> data;
    private boolean valid;
    private Map<String, Object> validationErrors;
    private Instant createdAt;

    public static FormSubmissionDto from(FormSubmission submission) {
        FormSubmissionDto dto = new FormSubmissionDto();
        dto.setId(submission.getId());
        dto.setFormDefinitionId(submission.getFormDefinition().getId());
        dto.setData(submission.getData());
        dto.setValid(submission.isValid());
        dto.setValidationErrors(submission.getValidationErrors());
        dto.setCreatedAt(submission.getCreatedAt());
        return dto;
    }
}
