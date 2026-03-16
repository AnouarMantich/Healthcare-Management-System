import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { Patient } from '../model/patient';
import { PatientService } from './patient.service';
import { catchError, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PatientsResolver implements Resolve<Patient[]> {
  constructor(private patientService: PatientService) {}

  resolve(): Observable<Patient[]> {
    // Fetch all patients
    return this.patientService.getAllPatients().pipe(
      catchError((error) => {
        console.error('Error loading patients in resolver', error);
        return of([]); // return empty array on error
      }),
    );
  }
}
