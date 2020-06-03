import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HomeService } from '../home.service';
import { AuthenticationService } from '../../../services/authentication.service';

import {User } from '../../../models/User';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  userDetailsForm: FormGroup;
  firstName: FormControl;
  lastName: FormControl;
  email: FormControl;
  state$: any;
  loggedinUser: User = new User();
   user: User = new User();
  constructor( private route: ActivatedRoute, private  homeService :HomeService, private authenticationService:AuthenticationService) {
    this.route.queryParams.subscribe(params => {
      console.log(params);
    });
   }

  ngOnInit(): void {
    
    this.user = JSON.parse(localStorage.getItem('currentUser'));
    this.homeService.getUserDetails(this.user.email)
     .subscribe(data=>{
      let user = new User();
      user["email"] = data["email"];
      user["firstName"] = data["firstName"];
      user["lastName"] = data["lastName"];
      this.authenticationService.login(user);

     }, error =>{
       console.log("Some issue in fetching user details");
     })

    }

  updateUser() {
    let user: User = new User();
    user["firstName"] = this.firstName.value;
    user["lastName"] = this.lastName.value;
    user["email"] = this.email.value;
    // user["password"] = this.pa
    this.homeService.updateUserDetails(user)
    .subscribe(data=>{
      alert("User details Updated Succesfully");
    }, error=>{
        alert("Issue Saving User details");
    })
  }

}
