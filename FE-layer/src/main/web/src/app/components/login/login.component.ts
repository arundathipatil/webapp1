import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { first } from 'rxjs/operators';
import {AuthenticationService} from '../../services/authentication.service';
import { LoginService } from './login.service';
import { User } from 'src/app/models/User';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  email : FormControl = new FormControl('', Validators.required);
  password: FormControl = new FormControl('', [Validators.required, Validators.nullValidator])

  resetPwdEmail : FormControl = new FormControl('', [Validators.required, Validators.email]);
  resetPwdForm : FormGroup = new FormGroup({
    resetPwdEmail: this.resetPwdEmail
});

  loginForm : FormGroup = new FormGroup({
        email: this.email,
        password: this.password
  });
  
    ngOnInit() {
    }

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private loginService: LoginService
    ) {
        // redirect to home if already logged in
        if (this.authenticationService.currentUserValue) { 
          let user = new User();
          user = JSON.parse(localStorage.getItem('currentUser'));
          let navigationExtras: NavigationExtras = {
            queryParams: {
                "email": user["email"],
                "firstName": user["firstName"],
                "lastName": user["lastName"],
            }
        };
          this.router.navigate(['home'], navigationExtras);
        }
    }

    login() {
        this.loginService.login(this.email.value, this.password.value)
        .subscribe(data=>{
          if(data) {

            this.loginService.getUserDeatislFromApi(this.email.value)
            .subscribe(data=>{
              let user = new User();
              user["email"] = data["email"];
              user["firstName"] = data["firstName"];
              user["lastName"] = data["lastName"];
              this.authenticationService.login(user);
              let navigationExtras: NavigationExtras = {
                queryParams: {
                    "email": data["email"],
                    "firstName": data["firstName"],
                    "lastName": data["lastName"],
                }
            };
            this.router.navigate(['home'], navigationExtras);
            }, error=>{
                alert("Issue fetching the user detials");
            });            
            
          } else {
            alert("User name password do not match");
          }
          
        }, error=>{
          alert("There was error Logging in!Please try latter")
        })
      }

      // not needed
      getUserDeatils(email: string) {
        // this.loginService.

      }

      sendResetPwdLink() {
        this.loginService.sendResetPwdEmail(this.resetPwdEmail.value)
        .subscribe(data=>{
            alert("Password reset link sent to email address " + this.resetPwdEmail.value);
        }, err=>{
          console.log("Unable to send reset password link");
        })
      }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
    // loginForm: FormGroup;
    // loading = false;
    // submitted = false;
    // returnUrl: string;

    

    
    // // convenience getter for easy access to form fields
    // get f() { return this.loginForm.controls; }

    // onSubmit() {
    //     this.submitted = true;

    //     // stop here if form is invalid
    //     if (this.loginForm.invalid) {
    //         return;
    //     }

    //     this.loading = true;
    //     this.authenticationService.login(this.f.username.value, this.f.password.value)
    //         .pipe(first())
    //         .subscribe(
    //             data => {
    //                 this.router.navigate([this.returnUrl]);
    //             },
    //             error => {
    //                 alert("there is an error");
    //                 this.loading = false;
    //             });
    // }

}
