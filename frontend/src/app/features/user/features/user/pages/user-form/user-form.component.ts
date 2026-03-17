import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../../user.service';
import { Router } from '@angular/router';
import { User } from '../../../../model/user';

@Component({
  selector: 'app-user-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})
export class UserFormComponent {
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
      this.userService.compleateProfile(this.patientForm.value).subscribe((data: User) => {
        void this.router.navigate(['/home']);
      });
      alert('Patient information submitted successfully!');
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }
}
