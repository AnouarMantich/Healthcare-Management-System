import { Component, computed, inject } from '@angular/core';
import { KeycloakService } from '../../core/services/keycloak.service';
import { Router, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  private readonly keycloakService = inject(KeycloakService);
  private router: Router = inject(Router);

  protected readonly currentUser = computed(() => this.keycloakService.getCurrentUser());

  logout() {
    void this.keycloakService.logout();
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
