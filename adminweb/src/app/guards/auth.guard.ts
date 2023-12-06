import {Injectable} from '@angular/core';
import {Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {environment} from "../../environments/environment";
import {AuthenticationService} from '../services/authentication.service';

@Injectable({providedIn: 'root'})
export class AuthGuard {

    noPerError = 'no_permission';

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentSession = this.authenticationService.currentSessionValue;
        if (currentSession) {
            return true;
        }
        // not logged in so redirect to login page with the return url
        this.router.navigate([environment.frontEndUrl.login], {queryParams: {returnUrl: state.url}});

        return false;
    }
}
