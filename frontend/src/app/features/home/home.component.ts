import { Component, computed, inject } from '@angular/core';
import { KeycloakService } from '../../core/services/keycloak.service';
import { HeaderComponent } from '../../shared/header.component/header.component';

@Component({
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  imports: [HeaderComponent],
})
export class HomeComponent {}
