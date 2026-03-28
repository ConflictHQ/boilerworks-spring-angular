import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { FormDefinition, FormSubmission, CreateFormRequest } from '../models/form.model';

@Injectable({
  providedIn: 'root',
})
export class FormService {
  private baseUrl = `${environment.apiUrl}/api/forms`;

  constructor(private http: HttpClient) {}

  list(): Observable<FormDefinition[]> {
    return this.http.get<FormDefinition[]>(this.baseUrl, { withCredentials: true });
  }

  get(id: string): Observable<FormDefinition> {
    return this.http.get<FormDefinition>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  create(request: CreateFormRequest): Observable<ApiResponse<FormDefinition>> {
    return this.http.post<ApiResponse<FormDefinition>>(this.baseUrl, request, {
      withCredentials: true,
    });
  }

  publish(id: string): Observable<ApiResponse<FormDefinition>> {
    return this.http.post<ApiResponse<FormDefinition>>(`${this.baseUrl}/${id}/publish`, {}, {
      withCredentials: true,
    });
  }

  archive(id: string): Observable<ApiResponse<FormDefinition>> {
    return this.http.post<ApiResponse<FormDefinition>>(`${this.baseUrl}/${id}/archive`, {}, {
      withCredentials: true,
    });
  }

  submit(id: string, data: Record<string, any>): Observable<ApiResponse<FormSubmission>> {
    return this.http.post<ApiResponse<FormSubmission>>(
      `${this.baseUrl}/${id}/submit`,
      { data },
      { withCredentials: true }
    );
  }

  getSubmissions(id: string): Observable<FormSubmission[]> {
    return this.http.get<FormSubmission[]>(`${this.baseUrl}/${id}/submissions`, {
      withCredentials: true,
    });
  }
}
