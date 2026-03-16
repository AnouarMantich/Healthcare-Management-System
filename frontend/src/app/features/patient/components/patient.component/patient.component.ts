import { PatientService } from './../../services/patient.service';
import { CommonModule } from '@angular/common';
import { Patient } from './../../model/patient';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-patient',
  imports: [CommonModule],
  templateUrl: './patient.component.html',
  styleUrl: './patient.component.css',
})
export class PatientComponent implements OnInit {
  patient!: Patient | null;
  loading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.patient = data['patient'] as Patient | null;
      this.loading = false;

      if (!this.patient) {
        console.warn('Patient not found, redirecting...');
        this.router.navigate(['/patients']); // fallback if patient not found
      }
    });
  }
}
