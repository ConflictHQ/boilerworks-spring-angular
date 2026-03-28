package com.boilerworks.api.forms.controller;

import com.boilerworks.api.dto.ApiResponse;
import com.boilerworks.api.forms.dto.*;
import com.boilerworks.api.forms.model.FormDefinition;
import com.boilerworks.api.forms.model.FormSubmission;
import com.boilerworks.api.forms.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @GetMapping
    @PreAuthorize("hasAuthority('form.view')")
    public List<FormDefinitionDto> list() {
        return formService.findAllForms();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('form.view')")
    public FormDefinitionDto get(@PathVariable UUID id) {
        return formService.findFormById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('form.add')")
    public ApiResponse<FormDefinitionDto> create(@Valid @RequestBody CreateFormRequest request) {
        FormDefinition form = formService.createForm(request);
        return ApiResponse.ok(FormDefinitionDto.from(form));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('form.change')")
    public ApiResponse<FormDefinitionDto> publish(@PathVariable UUID id) {
        FormDefinition form = formService.publishForm(id);
        return ApiResponse.ok(FormDefinitionDto.from(form));
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasAuthority('form.change')")
    public ApiResponse<FormDefinitionDto> archive(@PathVariable UUID id) {
        FormDefinition form = formService.archiveForm(id);
        return ApiResponse.ok(FormDefinitionDto.from(form));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('form.submit')")
    public ApiResponse<FormSubmissionDto> submit(@PathVariable UUID id,
                                                  @Valid @RequestBody SubmitFormRequest request) {
        FormSubmission submission = formService.submitForm(id, request);
        return ApiResponse.ok(FormSubmissionDto.from(submission));
    }

    @GetMapping("/{id}/submissions")
    @PreAuthorize("hasAuthority('form.view')")
    public List<FormSubmissionDto> submissions(@PathVariable UUID id) {
        return formService.findSubmissions(id);
    }
}
