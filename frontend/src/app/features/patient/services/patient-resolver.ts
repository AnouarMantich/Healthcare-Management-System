import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { Patient } from '../model/patient';
import { catchError, Observable, of } from 'rxjs';
import { PatientService } from './patient.service';

@Injectable({
  providedIn: 'root',
})
export class PatientResolver implements Resolve<Patient | null> {
  constructor(private patientService: PatientService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Patient | null> {
    const id = route.paramMap.get('id'); // get :id from URL
    if (!id) return of(null);

    return this.patientService.getPatient(id).pipe(
      catchError((error) => {
        console.error('Error loading patient', error);
        return of(null); // return null if error
      }),
    );
  }
}
