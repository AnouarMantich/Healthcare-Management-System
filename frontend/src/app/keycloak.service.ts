import { Injectable, signal } from '@angular/core';
import Keycloak, { KeycloakInstance, KeycloakProfile } from 'keycloak-js';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  private keycloak: KeycloakInstance | undefined;

  // Signals for easy consumption in components
  readonly isInitialized = signal(false);
  readonly isAuthenticated = signal(false);
  readonly profile = signal<KeycloakProfile | null>(null);

  init(): Promise<boolean> {
    if (this.keycloak) {
      return Promise.resolve(true);
    }

    this.keycloak = new Keycloak({
      url: 'http://localhost:8080',
      realm: 'starter-app',
      clientId: 'starter-client',
    });

    return this.keycloak
      .init({
        onLoad: 'check-sso',
        checkLoginIframe: false,
        pkceMethod: 'S256',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      })
      .then((authenticated) => {
        this.isInitialized.set(true);
        this.isAuthenticated.set(authenticated);

        if (authenticated) {
          return this.loadUserProfile().then(() => true);
        }

        return true;
      })
      .catch((error) => {
        // Keep the app running even if Keycloak fails, but mark as initialized
        console.error('Keycloak initialization failed', error);
        this.isInitialized.set(true);
        this.isAuthenticated.set(false);
        return false;
      });
  }

  login(): Promise<void> {
    if (!this.keycloak) {
      return Promise.reject('Keycloak not initialized');
    }
    return this.keycloak.login();
  }

  logout(): Promise<void> {
    if (!this.keycloak) {
      return Promise.reject('Keycloak not initialized');
    }
    this.isAuthenticated.set(false);
    this.profile.set(null);
    return this.keycloak.logout({
      redirectUri: window.location.origin,
    });
  }

  getToken(): Promise<string | undefined> {
    if (!this.keycloak) {
      return Promise.resolve(undefined);
    }

    const updateToken = this.keycloak.updateToken(30).catch((error) => {
      console.error('Failed to refresh token', error);
      return false;
    });

    return updateToken.then(() => this.keycloak?.token);
  }

  getCurrentUser(): KeycloakProfile | null {
    return this.profile();
  }

  private loadUserProfile(): Promise<void> {
    if (!this.keycloak) {
      return Promise.resolve();
    }

    return this.keycloak
      .loadUserProfile()
      .then((profile) => {
        this.profile.set(profile);
      })
      .catch((error) => {
        console.error('Failed to load user profile', error);
        this.profile.set(null);
      });
  }
}

