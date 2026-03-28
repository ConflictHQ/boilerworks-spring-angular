export interface WorkflowTransition {
  name: string;
  from: string;
  to: string;
  requiredPermission?: string;
}

export interface WorkflowStateMachine {
  states: string[];
  initial: string;
  terminal: string[];
  transitions: WorkflowTransition[];
}

export interface WorkflowDefinition {
  id: string;
  name: string;
  slug: string;
  description: string;
  version: number;
  active: boolean;
  stateMachine: WorkflowStateMachine;
  createdAt: string;
  updatedAt: string;
}

export interface WorkflowInstance {
  id: string;
  workflowDefinitionId: string;
  currentState: string;
  entityType: string;
  entityId: string;
  complete: boolean;
  contextData: any;
  createdAt: string;
  updatedAt: string;
}

export interface TransitionLog {
  id: string;
  transitionName: string;
  fromState: string;
  toState: string;
  performedBy: string;
  performedAt: string;
  comment: string;
  metadata: any;
}
