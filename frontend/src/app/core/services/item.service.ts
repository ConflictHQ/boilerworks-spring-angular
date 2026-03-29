import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Item, CreateItemRequest, UpdateItemRequest } from '../models/item.model';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private baseUrl = `${environment.apiUrl}/api/items`;

  constructor(private http: HttpClient) {}

  list(search?: string): Observable<Item[]> {
    let params = new HttpParams();
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<Item[]>(this.baseUrl, { params, withCredentials: true });
  }

  get(id: string): Observable<Item> {
    return this.http.get<Item>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  create(request: CreateItemRequest): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(this.baseUrl, request, { withCredentials: true });
  }

  update(id: string, request: UpdateItemRequest): Observable<ApiResponse<Item>> {
    return this.http.put<ApiResponse<Item>>(`${this.baseUrl}/${id}`, request, {
      withCredentials: true,
    });
  }

  delete(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`, { withCredentials: true });
  }
}
