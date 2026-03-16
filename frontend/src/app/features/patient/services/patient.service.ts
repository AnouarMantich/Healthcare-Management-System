import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  constructor(private httpClient: HttpClient) {}

  getAllPatients(): Observable<any> {
    return this.httpClient.get(`${environment.backendURL}/patient-service/api/v1/patients`);
  }
}
