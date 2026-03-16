import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { Patient } from '../model/patient';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  constructor(private httpClient: HttpClient) {}

  getAllPatients(): Observable<Patient[]> {
    return this.httpClient.get<Patient[]>(
      `${environment.backendURL}/patient-service/api/v1/patients`,
    );
  }

  getPatient(id: string): Observable<Patient> {
    return this.httpClient.get<Patient>(
      `${environment.backendURL}/patient-service/api/v1/patients/${id}`,
    );
  }
}
