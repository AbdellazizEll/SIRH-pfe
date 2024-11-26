import { Injectable } from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree} from '@angular/router';
import { TokenStorageService } from "../_services/token-storage.service";
import {map, Observable, take} from "rxjs";
import {AuthService} from "../_services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> {
    const requiredRoles = route.data['roles'] as Array<string>;

    return this.authService.isLoggedIn$.pipe(
      take(1), // Complete the observable after the first emission
      map(isLoggedIn => {
        if (!isLoggedIn) {
          // Redirect unauthenticated users to the login page
          return this.router.createUrlTree(['login']);
        }

        if (requiredRoles && requiredRoles.length > 0) {
          const user = this.authService.getUser();
          const roles = user?.roles || [];

          // Check if the user has at least one of the required roles
          const hasRole = requiredRoles.some(role => roles.includes(role));
          if (!hasRole) {
            // Redirect unauthorized users to the home page or an unauthorized page
            return this.router.createUrlTree(['home']);
          }
        }

        // Allow access
        return true;
      })
    );
  }
}
