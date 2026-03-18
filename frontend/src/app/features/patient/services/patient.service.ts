import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { Patient } from '../model/patient';
import { PatientUpdate } from '../model/patient-update';

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

  getPatientByCin(cin: string): Observable<Patient> {
    return this.httpClient.get<Patient>(
      `${environment.backendURL}/patient-service/api/v1/patients/findBy?cin=${cin}`,
    );
  }

  updatePatient(id: String, patient: PatientUpdate): Observable<Patient> {
    return this.httpClient.put<Patient>(
      `${environment.backendURL}/patient-service/api/v1/patients/${id}`,
      patient,
    );
  }
}
