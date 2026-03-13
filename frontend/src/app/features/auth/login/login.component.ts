import { UserService } from './../../user/user.service';
import { Component, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from '../../../core/services/keycloak.service';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private readonly keycloakService = inject(KeycloakService);
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);

  protected readonly appName = 'Starter Medical Portal';

  protected readonly isInitialized = this.keycloakService.isInitialized;
  protected readonly isAuthenticated = this.keycloakService.isAuthenticated;

  constructor() {
    effect(() => {
      if (this.isInitialized() && this.isAuthenticated()) {
        this.userService.checkUser().subscribe((data) => {
          if (data.profileCompleted) {
            void this.router.navigate(['/home']);
          } else void this.router.navigate(['/profile-update']);
        });
      }
    });
  }

  login() {
    void this.keycloakService.login();
  }
}
