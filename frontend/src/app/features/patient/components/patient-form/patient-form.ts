import { Patient } from './../../model/patient';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientService } from './../../services/patient.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BloodType, PatientUpdate } from '../../model/patient-update';

@Component({
  selector: 'app-patient-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './patient-form.html',
  styleUrl: './patient-form.css',
})
export class PatientForm implements OnInit {
  patientForm!: FormGroup;
  patientId: string = '';
  patient: Patient | null = null;

  bloodTypes = Object.values(BloodType);

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.patientId = this.route.snapshot.paramMap.get('id') || '';
    this.route.data.subscribe((data) => {
      this.patient = data['patient'] as Patient | null;

      if (!this.patient) {
        console.warn('Patient not found, redirecting...');
        this.router.navigate(['/patients']); // fallback if patient not found
      }
    });
    this.patientForm = this.fb.group({
      bloodType: [this.patient?.bloodType, Validators.required],
      allergies: [this.patient?.allergies],
      chronicDiseases: [this.patient?.insuranceNumber],
      insuranceNumber: [this.patient?.insuranceNumber],
      emergencyContact: [this.patient?.emergencyContact, Validators.required],
    });
  }

  onSubmit() {
    if (this.patientForm.valid) {
      const formValue = this.patientForm.value;

      // convert comma-separated strings to arrays
      formValue.allergies = formValue.allergies
        ? formValue.allergies.split(',').map((a: string) => a.trim())
        : [];

      formValue.chronicDiseases = formValue.chronicDiseases
        ? formValue.chronicDiseases.split(',').map((d: string) => d.trim())
        : [];

      const patientUpdate: PatientUpdate = {
        bloodType: formValue.bloodTypes,
        allergies: [...new Set([...(this.patient?.allergies || []), ...formValue.allergies])],
        chronicDiseases: [
          ...new Set([...(this.patient?.chronicDiseases || []), ...formValue.chronicDiseases]),
        ],
        insuranceNumber: formValue.insuranceNumber,
        emergencyContact: formValue.emergencyContact,
      };

      this.patientService.updatePatient(this.patientId, patientUpdate).subscribe((data) => {
        this.router.navigate(['/patients', this.patientId]);
      });
    }
  }
}
