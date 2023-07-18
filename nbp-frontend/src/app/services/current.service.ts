import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CurrentRequest } from '../models/currentRequest';
import { CurrentResponse } from '../models/currentResponse';
import { UserActivity } from '../models/userActivity';

@Injectable({
  providedIn: 'root',
})
export class CurrentService {
  private apiUrl = 'http://localhost:8080/currencies/';

  constructor(private http: HttpClient) {}

  getCurrent(currentRequest: CurrentRequest): Observable<CurrentResponse> {
    return this.http.post<CurrentResponse>(
      this.apiUrl + 'get-current-currency-value-command',
      currentRequest
    );
  }

  getUserActivity(): Observable<UserActivity[]> {
    return this.http.get<UserActivity[]>(this.apiUrl + 'requests');
  }
}
