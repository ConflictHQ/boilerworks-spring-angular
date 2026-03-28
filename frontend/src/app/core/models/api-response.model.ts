export interface FieldError {
  field: string;
  messages: string[];
}

export interface ApiResponse<T> {
  ok: boolean;
  data: T;
  errors?: FieldError[];
}
