import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-patient',
  imports: [],
  templateUrl: './add-patient.html',
  styleUrl: './add-patient.css',
})
export class AddPatient implements OnInit {

  patientForm!: FormGroup;
  submitted = false;
  successMessage = '';

  constructor(private fb: FormBuilder, private patientService: PatientService) { }

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      birthDate: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(/^\+\d{9,15}$/)]],
      address: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  get f() { return this.patientForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.successMessage = '';

    if (this.patientForm.invalid) {
      return;
    }

    this.patientService.createPatient(this.patientForm.value).subscribe({
      next: (res) => {
        this.successMessage = 'Patient saved successfully!';
        this.patientForm.reset();
        this.submitted = false;
      },
      error: (err) => console.error(err)
    });
  }
}