package com.boilerworks.api.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.boilerworks.api.TestConfig;
import com.boilerworks.api.workflow.dto.*;
import com.boilerworks.api.workflow.model.WorkflowDefinition;
import com.boilerworks.api.workflow.model.WorkflowInstance;
import com.boilerworks.api.workflow.repository.WorkflowDefinitionRepository;
import com.boilerworks.api.workflow.repository.WorkflowInstanceRepository;
import com.boilerworks.api.workflow.service.WorkflowService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class WorkflowServiceTest {

  @Autowired private WorkflowService workflowService;

  @Autowired private WorkflowDefinitionRepository definitionRepository;

  @Autowired private WorkflowInstanceRepository instanceRepository;

  @BeforeEach
  void setUp() {
    instanceRepository.deleteAll();
    definitionRepository.deleteAll();
  }

  private WorkflowDefinition createApprovalWorkflow() {
    CreateWorkflowRequest request = new CreateWorkflowRequest();
    request.setName("Approval Workflow");
    request.setDescription("Simple approval workflow");
    request.setStateMachine(
        Map.of(
            "states", List.of("draft", "review", "approved", "rejected"),
            "initial", "draft",
            "terminal", List.of("approved", "rejected"),
            "transitions",
                List.of(
                    Map.of("name", "submit", "from", "draft", "to", "review"),
                    Map.of("name", "approve", "from", "review", "to", "approved"),
                    Map.of("name", "reject", "from", "review", "to", "rejected"),
                    Map.of("name", "revise", "from", "rejected", "to", "draft"))));
    return workflowService.createDefinition(request);
  }

  @Test
  void createWorkflowDefinition() {
    WorkflowDefinition def = createApprovalWorkflow();
    assertThat(def.getName()).isEqualTo("Approval Workflow");
    assertThat(def.getSlug()).isEqualTo("approval-workflow");
    assertThat(def.isActive()).isTrue();
  }

  @Test
  @WithMockUser
  void startWorkflowInstance() {
    WorkflowDefinition def = createApprovalWorkflow();

    StartInstanceRequest request = new StartInstanceRequest();
    request.setEntityType("Item");
    request.setEntityId("test-123");

    WorkflowInstance instance = workflowService.startInstance(def.getId(), request);
    assertThat(instance.getCurrentState()).isEqualTo("draft");
    assertThat(instance.isComplete()).isFalse();
    assertThat(instance.getEntityType()).isEqualTo("Item");
  }

  @Test
  @WithMockUser
  void performTransition() {
    WorkflowDefinition def = createApprovalWorkflow();

    StartInstanceRequest startReq = new StartInstanceRequest();
    WorkflowInstance instance = workflowService.startInstance(def.getId(), startReq);

    TransitionRequest transReq = new TransitionRequest();
    transReq.setTransition("submit");
    transReq.setComment("Ready for review");

    WorkflowInstance updated = workflowService.performTransition(instance.getId(), transReq);
    assertThat(updated.getCurrentState()).isEqualTo("review");
    assertThat(updated.isComplete()).isFalse();

    // Check transition history
    List<TransitionLogDto> history = workflowService.getTransitionHistory(instance.getId());
    assertThat(history).hasSize(1);
    assertThat(history.getFirst().getTransitionName()).isEqualTo("submit");
    assertThat(history.getFirst().getFromState()).isEqualTo("draft");
    assertThat(history.getFirst().getToState()).isEqualTo("review");
  }

  @Test
  @WithMockUser
  void transitionToTerminalState() {
    WorkflowDefinition def = createApprovalWorkflow();

    StartInstanceRequest startReq = new StartInstanceRequest();
    WorkflowInstance instance = workflowService.startInstance(def.getId(), startReq);

    TransitionRequest submitReq = new TransitionRequest();
    submitReq.setTransition("submit");
    workflowService.performTransition(instance.getId(), submitReq);

    TransitionRequest approveReq = new TransitionRequest();
    approveReq.setTransition("approve");
    WorkflowInstance approved = workflowService.performTransition(instance.getId(), approveReq);

    assertThat(approved.getCurrentState()).isEqualTo("approved");
    assertThat(approved.isComplete()).isTrue();
  }

  @Test
  @WithMockUser
  void invalidTransitionThrows() {
    WorkflowDefinition def = createApprovalWorkflow();

    StartInstanceRequest startReq = new StartInstanceRequest();
    WorkflowInstance instance = workflowService.startInstance(def.getId(), startReq);

    TransitionRequest req = new TransitionRequest();
    req.setTransition("approve");

    assertThatThrownBy(() -> workflowService.performTransition(instance.getId(), req))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid transition");
  }
}
