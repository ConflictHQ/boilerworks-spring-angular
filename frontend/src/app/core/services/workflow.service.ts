import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { WorkflowDefinition, WorkflowInstance, TransitionLog } from '../models/workflow.model';

@Injectable({
  providedIn: 'root',
})
export class WorkflowService {
  private baseUrl = `${environment.apiUrl}/api/workflows`;

  constructor(private http: HttpClient) {}

  listDefinitions(): Observable<WorkflowDefinition[]> {
    return this.http.get<WorkflowDefinition[]>(this.baseUrl, { withCredentials: true });
  }

  getDefinition(id: string): Observable<WorkflowDefinition> {
    return this.http.get<WorkflowDefinition>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  createDefinition(request: any): Observable<ApiResponse<WorkflowDefinition>> {
    return this.http.post<ApiResponse<WorkflowDefinition>>(this.baseUrl, request, {
      withCredentials: true,
    });
  }

  startInstance(definitionId: string, request: any): Observable<ApiResponse<WorkflowInstance>> {
    return this.http.post<ApiResponse<WorkflowInstance>>(
      `${this.baseUrl}/${definitionId}/start`,
      request,
      { withCredentials: true },
    );
  }

  getInstance(instanceId: string): Observable<WorkflowInstance> {
    return this.http.get<WorkflowInstance>(`${this.baseUrl}/instances/${instanceId}`, {
      withCredentials: true,
    });
  }

  performTransition(
    instanceId: string,
    transition: string,
    comment?: string,
  ): Observable<ApiResponse<WorkflowInstance>> {
    return this.http.post<ApiResponse<WorkflowInstance>>(
      `${this.baseUrl}/instances/${instanceId}/transition`,
      { transition, comment },
      { withCredentials: true },
    );
  }

  getTransitionHistory(instanceId: string): Observable<TransitionLog[]> {
    return this.http.get<TransitionLog[]>(`${this.baseUrl}/instances/${instanceId}/history`, {
      withCredentials: true,
    });
  }
}
