package com.boilerworks.api.workflow.service;

import com.boilerworks.api.security.BoilerworksUserDetails;
import com.boilerworks.api.service.SlugUtil;
import com.boilerworks.api.workflow.dto.*;
import com.boilerworks.api.workflow.model.WorkflowDefinition;
import com.boilerworks.api.workflow.model.WorkflowInstance;
import com.boilerworks.api.workflow.model.WorkflowTransitionLog;
import com.boilerworks.api.workflow.repository.WorkflowDefinitionRepository;
import com.boilerworks.api.workflow.repository.WorkflowInstanceRepository;
import com.boilerworks.api.workflow.repository.WorkflowTransitionLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowService {

    private final WorkflowDefinitionRepository definitionRepository;
    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowTransitionLogRepository transitionLogRepository;

    @Transactional(readOnly = true)
    public List<WorkflowDefinitionDto> findAllDefinitions() {
        return definitionRepository.findAll().stream()
            .map(WorkflowDefinitionDto::from).toList();
    }

    @Transactional(readOnly = true)
    public WorkflowDefinitionDto findDefinitionById(UUID id) {
        WorkflowDefinition def = definitionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Workflow definition not found"));
        return WorkflowDefinitionDto.from(def);
    }

    public WorkflowDefinition createDefinition(CreateWorkflowRequest request) {
        WorkflowDefinition def = new WorkflowDefinition();
        def.setName(request.getName());
        def.setSlug(SlugUtil.slugify(request.getName()));
        def.setDescription(request.getDescription());
        def.setStateMachine(request.getStateMachine());
        def.setVersion(1);
        def.setActive(true);
        return definitionRepository.save(def);
    }

    @SuppressWarnings("unchecked")
    public WorkflowInstance startInstance(UUID definitionId, StartInstanceRequest request) {
        WorkflowDefinition def = definitionRepository.findById(definitionId)
            .orElseThrow(() -> new EntityNotFoundException("Workflow definition not found"));

        if (!def.isActive()) {
            throw new IllegalArgumentException("Workflow definition is not active");
        }

        Map<String, Object> sm = def.getStateMachine();
        String initialState = (String) sm.get("initial");
        if (initialState == null) {
            throw new IllegalArgumentException("Workflow has no initial state defined");
        }

        WorkflowInstance instance = new WorkflowInstance();
        instance.setWorkflowDefinition(def);
        instance.setCurrentState(initialState);
        instance.setEntityType(request.getEntityType());
        instance.setEntityId(request.getEntityId());
        instance.setContextData(request.getContextData());
        instance.setComplete(false);

        return instanceRepository.save(instance);
    }

    @SuppressWarnings("unchecked")
    public WorkflowInstance performTransition(UUID instanceId, TransitionRequest request) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
            .orElseThrow(() -> new EntityNotFoundException("Workflow instance not found"));

        if (instance.isComplete()) {
            throw new IllegalArgumentException("Workflow instance is already complete");
        }

        Map<String, Object> sm = instance.getWorkflowDefinition().getStateMachine();
        List<Map<String, Object>> transitions = (List<Map<String, Object>>) sm.get("transitions");
        List<String> terminalStates = (List<String>) sm.get("terminal");

        Map<String, Object> matchingTransition = null;
        if (transitions != null) {
            for (Map<String, Object> t : transitions) {
                if (request.getTransition().equals(t.get("name"))
                    && instance.getCurrentState().equals(t.get("from"))) {
                    matchingTransition = t;
                    break;
                }
            }
        }

        if (matchingTransition == null) {
            throw new IllegalArgumentException(
                "Invalid transition '" + request.getTransition() +
                "' from state '" + instance.getCurrentState() + "'");
        }

        String fromState = instance.getCurrentState();
        String toState = (String) matchingTransition.get("to");

        // Log the transition
        WorkflowTransitionLog log = new WorkflowTransitionLog();
        log.setWorkflowInstance(instance);
        log.setTransitionName(request.getTransition());
        log.setFromState(fromState);
        log.setToState(toState);
        log.setPerformedBy(getCurrentUserId());
        log.setPerformedAt(Instant.now());
        log.setComment(request.getComment());
        transitionLogRepository.save(log);

        // Update instance
        instance.setCurrentState(toState);
        if (terminalStates != null && terminalStates.contains(toState)) {
            instance.setComplete(true);
        }

        return instanceRepository.save(instance);
    }

    @Transactional(readOnly = true)
    public WorkflowInstanceDto findInstanceById(UUID id) {
        WorkflowInstance instance = instanceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Workflow instance not found"));
        return WorkflowInstanceDto.from(instance);
    }

    @Transactional(readOnly = true)
    public List<TransitionLogDto> getTransitionHistory(UUID instanceId) {
        return transitionLogRepository.findByWorkflowInstanceIdOrderByPerformedAtAsc(instanceId)
            .stream().map(TransitionLogDto::from).toList();
    }

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof BoilerworksUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}
