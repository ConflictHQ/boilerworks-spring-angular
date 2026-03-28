package com.boilerworks.api.workflow.controller;

import com.boilerworks.api.dto.ApiResponse;
import com.boilerworks.api.workflow.dto.*;
import com.boilerworks.api.workflow.model.WorkflowDefinition;
import com.boilerworks.api.workflow.model.WorkflowInstance;
import com.boilerworks.api.workflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @GetMapping
    @PreAuthorize("hasAuthority('workflow.view')")
    public List<WorkflowDefinitionDto> listDefinitions() {
        return workflowService.findAllDefinitions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('workflow.view')")
    public WorkflowDefinitionDto getDefinition(@PathVariable UUID id) {
        return workflowService.findDefinitionById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('workflow.add')")
    public ApiResponse<WorkflowDefinitionDto> createDefinition(
            @Valid @RequestBody CreateWorkflowRequest request) {
        WorkflowDefinition def = workflowService.createDefinition(request);
        return ApiResponse.ok(WorkflowDefinitionDto.from(def));
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAuthority('workflow.execute')")
    public ApiResponse<WorkflowInstanceDto> startInstance(
            @PathVariable UUID id,
            @RequestBody StartInstanceRequest request) {
        WorkflowInstance instance = workflowService.startInstance(id, request);
        return ApiResponse.ok(WorkflowInstanceDto.from(instance));
    }

    @GetMapping("/instances/{instanceId}")
    @PreAuthorize("hasAuthority('workflow.view')")
    public WorkflowInstanceDto getInstance(@PathVariable UUID instanceId) {
        return workflowService.findInstanceById(instanceId);
    }

    @PostMapping("/instances/{instanceId}/transition")
    @PreAuthorize("hasAuthority('workflow.execute')")
    public ApiResponse<WorkflowInstanceDto> transition(
            @PathVariable UUID instanceId,
            @Valid @RequestBody TransitionRequest request) {
        WorkflowInstance instance = workflowService.performTransition(instanceId, request);
        return ApiResponse.ok(WorkflowInstanceDto.from(instance));
    }

    @GetMapping("/instances/{instanceId}/history")
    @PreAuthorize("hasAuthority('workflow.view')")
    public List<TransitionLogDto> transitionHistory(@PathVariable UUID instanceId) {
        return workflowService.getTransitionHistory(instanceId);
    }
}
