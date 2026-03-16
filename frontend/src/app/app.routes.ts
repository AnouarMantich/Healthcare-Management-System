import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { HomeComponent } from './features/home/home.component';
import { PatientFormComponent } from './features/user/features/user/pages/patient-form/patient-form.component';
import { PatientsListComponent } from './features/patient/components/patients-list.component/patients-list.component';
import { PatientComponent } from './features/patient/components/patient.component/patient.component';
import { PatientsResolver } from './features/patient/services/patients-resolver';
import { PatientResolver } from './features/patient/services/patient-resolver';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'patients', component: PatientsListComponent, resolve: { patients: PatientsResolver } },
  { path: 'patients/:id', component: PatientComponent, resolve: { patient: PatientResolver } },
  { path: 'profile-update', component: PatientFormComponent },
  { path: '**', redirectTo: 'login' },
];
