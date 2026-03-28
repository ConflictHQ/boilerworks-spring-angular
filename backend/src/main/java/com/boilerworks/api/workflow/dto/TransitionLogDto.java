package com.boilerworks.api.workflow.dto;

import com.boilerworks.api.workflow.model.WorkflowTransitionLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransitionLogDto {

    private UUID id;
    private String transitionName;
    private String fromState;
    private String toState;
    private UUID performedBy;
    private Instant performedAt;
    private String comment;
    private Map<String, Object> metadata;

    public static TransitionLogDto from(WorkflowTransitionLog log) {
        TransitionLogDto dto = new TransitionLogDto();
        dto.setId(log.getId());
        dto.setTransitionName(log.getTransitionName());
        dto.setFromState(log.getFromState());
        dto.setToState(log.getToState());
        dto.setPerformedBy(log.getPerformedBy());
        dto.setPerformedAt(log.getPerformedAt());
        dto.setComment(log.getComment());
        dto.setMetadata(log.getMetadata());
        return dto;
    }
}
