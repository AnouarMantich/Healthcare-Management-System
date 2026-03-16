import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { User } from './model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  checkUser(): Observable<User> {
    return this.httpClient.get<User>(environment.backendURL + '/user-service/api/v1/users/me');
  }

  compleateProfile(updateUser: any): Observable<User> {
    return this.httpClient.put<User>(
      environment.backendURL + '/user-service/api/v1/users',
      updateUser,
    );
  }
}
