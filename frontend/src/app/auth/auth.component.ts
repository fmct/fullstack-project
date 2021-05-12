import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthResponseData, AuthService } from './auth.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html'
})
export class AuthComponent implements OnInit {
  isLoginMode = true;
  isLoading = false;
  error: string = null;
  signupForm: FormGroup;

  constructor(private authService: AuthService, private router: Router) {}

  onSwitchMode() {
    this.isLoginMode = !this.isLoginMode;
  }

  getSubmitAuthText() {
    return this.isLoginMode ? 'Login' : 'Sign up';
  }
  
  getSwitchAuthText() {
    return this.isLoginMode ? 'Sign up' : 'Login';
  }

  ngOnInit() {
    this.signupForm = new FormGroup({
      'username': new FormControl(null, [ Validators.required ]),
      'password': new FormControl(null, [ Validators.required, Validators.minLength(6) ]),
    });
    
  }

  onSubmit() {
    if (!this.signupForm.valid) {
      return;
    }
    const { username, password } = this.signupForm.value;
    
    let authObs: Observable<AuthResponseData>;

    this.isLoading = true;

    if(!this.isLoginMode) {
      authObs = this.authService.signup(username,password);
    } else {
      authObs = this.authService.login(username,password);
    }
    
    authObs.subscribe(
      respData => {
        this.error = null;
        this.isLoading = false;
        this.router.navigate(['/products']);
      },
      errorMessage => {
        this.error = errorMessage;
        this.isLoading = false;
      }
    );
    
    this.signupForm.reset();
  }

}
