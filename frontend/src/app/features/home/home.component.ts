import { Component, computed, inject } from '@angular/core';
import { KeycloakService } from '../../core/services/keycloak.service';

@Component({
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  private readonly keycloakService = inject(KeycloakService);

  protected readonly currentUser = computed(() => this.keycloakService.getCurrentUser());

  logout() {
    void this.keycloakService.logout();
  }
}
