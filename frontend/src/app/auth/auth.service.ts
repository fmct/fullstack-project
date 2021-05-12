import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, throwError } from "rxjs";
import { catchError, mergeMap, tap } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { User } from "./user.model";

export interface AuthResponseData {
    username?: string;
    id?: number;
    token?: string;
    expiresIn?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    user = new BehaviorSubject<User>(null);
    private tokenExpirationTimer!: any;

    constructor(private http: HttpClient, private router: Router) {}

    signup(username: string, password: string) {
        return this.http.post<AuthResponseData>(
            `${environment.backendUri}/create`, 
            { 
                username,
                password
            }   
        ).pipe(
            catchError(this.handleError),
            mergeMap(() => {
                return this.login(username, password);
            })
        );
    }

    login(username: string, password: string) {
        return this.http.post<AuthResponseData>(
            `${environment.backendUri}/authenticate`, 
            { 
                username,
                password
            }   
        ).pipe(
            catchError(this.handleError),
            tap(resData => {
                let expirationDate: Date = null;
                if(resData.expiresIn) {
                    expirationDate = new Date(resData.expiresIn);
                    this.authLogout(expirationDate.getTime() - new Date().getTime());
                }
                const user = new User(username, resData.token, expirationDate);
                this.user.next(user);
                localStorage.setItem('userData', JSON.stringify(user));
            })
        );
    }

    autoLogin() {

        const userData: {
            username: string;
            _token: string;
            _tokenExpirationDate?: string;

        } = JSON.parse(localStorage.getItem('userData'));

        if(!userData) {
            return;
        }


        const loadedUser = new User(
            userData.username, 
            userData._token, 
            userData._tokenExpirationDate ? new Date(userData._tokenExpirationDate) : null
        )
        
        
        if(loadedUser.token) {
            this.user.next(loadedUser);
            if(loadedUser.tokenExpirationDate) {
                const expirationDuration = loadedUser.tokenExpirationDate.getTime() - new Date().getTime();
                this.authLogout(expirationDuration);
            }
        }

    }

    logout() {
        this.user.next(null);
        localStorage.removeItem('userData');
        this.router.navigate(['/auth']);
        if(this.tokenExpirationTimer) {
            clearTimeout(this.tokenExpirationTimer);
        }
    }

    authLogout(expirationDuration: number) {
        this.tokenExpirationTimer = setTimeout(()=> {
            this.logout();
        }, expirationDuration);

    }

    private handleError(errorResp: HttpErrorResponse) {
        const { error, message } = errorResp;
        if(typeof error === "string") {
            return throwError(message.split(": ")[0] + ": " + errorResp.status + " "  + error);
        }
        return throwError(message.split(": ")[0] + ": " + errorResp.status + " "  + (error.message || errorResp.statusText));
    }
}