package com.boilerworks.api.forms.service;

import com.boilerworks.api.forms.dto.*;
import com.boilerworks.api.forms.model.FormDefinition;
import com.boilerworks.api.forms.model.FormStatus;
import com.boilerworks.api.forms.model.FormSubmission;
import com.boilerworks.api.forms.repository.FormDefinitionRepository;
import com.boilerworks.api.forms.repository.FormSubmissionRepository;
import com.boilerworks.api.service.SlugUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormService {

  private final FormDefinitionRepository formDefinitionRepository;
  private final FormSubmissionRepository formSubmissionRepository;
  private final FormValidationService validationService;

  @Transactional(readOnly = true)
  public List<FormDefinitionDto> findAllForms() {
    return formDefinitionRepository.findAll().stream().map(FormDefinitionDto::from).toList();
  }

  @Transactional(readOnly = true)
  public FormDefinitionDto findFormById(UUID id) {
    FormDefinition form =
        formDefinitionRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Form not found"));
    return FormDefinitionDto.from(form);
  }

  public FormDefinition createForm(CreateFormRequest request) {
    FormDefinition form = new FormDefinition();
    form.setName(request.getName());
    form.setSlug(SlugUtil.slugify(request.getName()));
    form.setDescription(request.getDescription());
    form.setSchema(request.getSchema());
    form.setLogicRules(request.getLogicRules());
    form.setStatus(FormStatus.DRAFT);
    form.setVersion(1);
    return formDefinitionRepository.save(form);
  }

  public FormDefinition publishForm(UUID id) {
    FormDefinition form =
        formDefinitionRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Form not found"));
    form.setStatus(FormStatus.PUBLISHED);
    return formDefinitionRepository.save(form);
  }

  public FormDefinition archiveForm(UUID id) {
    FormDefinition form =
        formDefinitionRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Form not found"));
    form.setStatus(FormStatus.ARCHIVED);
    return formDefinitionRepository.save(form);
  }

  public FormSubmission submitForm(UUID formId, SubmitFormRequest request) {
    FormDefinition form =
        formDefinitionRepository
            .findById(formId)
            .orElseThrow(() -> new EntityNotFoundException("Form not found"));

    if (form.getStatus() != FormStatus.PUBLISHED) {
      throw new IllegalArgumentException("Form is not published");
    }

    Map<String, List<String>> errors =
        validationService.validate(form.getSchema(), request.getData());

    FormSubmission submission = new FormSubmission();
    submission.setFormDefinition(form);
    submission.setData(request.getData());
    submission.setValid(errors.isEmpty());
    if (!errors.isEmpty()) {
      submission.setValidationErrors(Map.of("fields", errors));
    }

    return formSubmissionRepository.save(submission);
  }

  @Transactional(readOnly = true)
  public List<FormSubmissionDto> findSubmissions(UUID formId) {
    return formSubmissionRepository.findByFormDefinitionId(formId).stream()
        .map(FormSubmissionDto::from)
        .toList();
  }
}
