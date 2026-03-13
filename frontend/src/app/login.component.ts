import { Component, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from './keycloak.service';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private readonly keycloakService = inject(KeycloakService);
  private readonly router = inject(Router);

  protected readonly appName = 'Starter Medical Portal';

  protected readonly isInitialized = this.keycloakService.isInitialized;
  protected readonly isAuthenticated = this.keycloakService.isAuthenticated;

  constructor() {
    effect(() => {
      if (this.isInitialized() && this.isAuthenticated()) {
        void this.router.navigate(['/home']);
      }
    });
  }

  login() {
    void this.keycloakService.login();
  }
}

