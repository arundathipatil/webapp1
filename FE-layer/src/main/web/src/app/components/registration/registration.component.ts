import { Component, OnInit } from '@angular/core';

import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';

import { AuthenticationService } from '../../services/authentication.service';
import { User } from 'src/app/models/User';
import { RegistrationService } from './registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {


  registrationForm: FormGroup;
  firstName: FormControl;
  lastName: FormControl;
  email: FormControl;
  password: FormControl;
  confirmPassword: FormControl

  constructor(private registrationService: RegistrationService, private route: Router) { }

  ngOnInit(): void {
    this.firstName = new FormControl('', Validators.required);
    this.lastName = new FormControl('', Validators.required);
    this.email = new FormControl('', [
      Validators.required,
      Validators.email
    ]);
    this.password = new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(32),
      // Validators.pattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\]).{8,32}")
      // Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}$")
      Validators.pattern('(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!#^~%*?&,.<>"\'\\;:\{\\\}\\\[\\\]\\\|\\\+\\\-\\\=\\\_\\\)\\\(\\\)\\\`\\\/\\\\\\]])[A-Za-z0-9\d$@].{7,}')
    ]);
    // this.confirmPassword = new FormControl('',
    //   [Validators.compose(
    //     [Validators.required,
    //     // Validators.pattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\]).{8,32}"),
    //     // this.validatePassword.bind(this)
    //   ]
    //   )]
    // );

    this.registrationForm = new FormGroup({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password
      // confirmPassword: this.confirmPassword
    })
  }

  registerUser() {

    let user: User = {
      firstName: this.firstName.value,
      lastName: this.lastName.value,
      email: this.email.value,
      password: this.password.value
    }
    console.warn(this.registrationForm.value);
    this.registrationService.registerUser(user)
      .subscribe((data => {
        if(data != null){
          alert("User created Successfully. Please login");
        } else {
          alert("User email already exsists!Please use different email id")
        }
       
        this.route.navigateByUrl("");
      }), error => {
        alert("There was a problem creating User! Please try again later");
      });
  }

  validatePassword(confirmControl: FormControl): { [key: string]: boolean } | null  {
    if(this.password.value == this.confirmPassword.value) {
      return { 'ageRange': false };
    } else {
      return { 'ageRange': true };;
    }
    
  }

}
