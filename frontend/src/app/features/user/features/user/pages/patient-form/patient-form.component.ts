import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-patient-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './patient-form.component.html',
  styleUrls: ['./patient-form.component.css'],
})
export class PatientFormComponent {
  patientForm: FormGroup;
  private readonly router = inject(Router);

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
  ) {
    this.patientForm = this.fb.group({
      address: ['', Validators.required],
      phone: ['', [Validators.required]],
      cin: ['', [Validators.required]],
    });
  }

  submit() {
    if (this.patientForm.valid) {
      this.userService.compleateProfile(this.patientForm.value).subscribe((data) => {
        void this.router.navigate(['/home']);
      });
      alert('Patient information submitted successfully!');
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }
}
