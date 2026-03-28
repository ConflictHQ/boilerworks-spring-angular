export interface FormField {
  name: string;
  type: 'text' | 'number' | 'email' | 'select' | 'checkbox' | 'textarea' | 'date';
  label: string;
  required?: boolean;
  options?: string[];
  min?: number;
  max?: number;
  maxLength?: number;
  pattern?: string;
}

export interface FormSchema {
  fields: FormField[];
}

export interface FormDefinition {
  id: string;
  name: string;
  slug: string;
  description: string;
  version: number;
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
  schema: FormSchema;
  logicRules: any;
  createdAt: string;
  updatedAt: string;
}

export interface FormSubmission {
  id: string;
  formDefinitionId: string;
  data: Record<string, any>;
  valid: boolean;
  validationErrors: any;
  createdAt: string;
}

export interface CreateFormRequest {
  name: string;
  description?: string;
  schema: FormSchema;
  logicRules?: any;
}
