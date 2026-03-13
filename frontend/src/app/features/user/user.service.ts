import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  checkUser(): Observable<any> {
    return this.httpClient.get(environment.backendURL + '/user-service/api/v1/users/me');
  }

  compleateProfile(updateUser: any): Observable<any> {
    return this.httpClient.put(environment.backendURL + '/user-service/api/v1/users', updateUser);
  }
}
