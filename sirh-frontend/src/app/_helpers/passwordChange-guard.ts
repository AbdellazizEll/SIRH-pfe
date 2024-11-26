import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from "../_services/token-storage.service";
import { AuthService } from "../_services/auth.service";
import { map, take } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PasswordChangeGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.authService.isLoggedIn$.pipe(
      take(1),
      map(isLoggedIn => {
        if (!isLoggedIn) {
          return this.router.createUrlTree(['login']);
        }

        const user = this.authService.getUser();
        if (user && user.needsPasswordChange) {
          console.log("Redirecting to change password with message."); // Debugging line
          // Redirect to the change password page with a query parameter
          return this.router.createUrlTree(['changePassword'], {
            queryParams: { message: 'Veuillez changer votre mot de passe pour continuer.' }
          });
        }

        return true;
      })
    );
  }
}
