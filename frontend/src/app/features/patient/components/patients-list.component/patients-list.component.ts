import { Component, OnChanges, OnInit } from '@angular/core';
import { PatientService } from '../../services/patient.service';
import { Patient } from '../../model/patient';
import { PatientComponent } from '../patient.component/patient.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patients-list.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './patients-list.component.html',
  styleUrl: './patients-list.component.css',
})
export class PatientsListComponent implements OnInit {
  patients: Patient[] = [];
  loading: boolean = true;
  searchText: string = '';
  searchField: string = 'fullName';
  filteredPatients: Patient[] = []; // filtered

  constructor(
    private route: ActivatedRoute,
    private patientService: PatientService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    // Get resolved data
    this.route.data.subscribe((data: any) => {
      this.patients = data.patients;
      this.loading = false;
      console.log('Patients loaded via resolver:', this.patients);
    });
  }

  goToPatient(id: string) {
    this.router.navigate(['/patients', id]);
  }
}
