import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationStart } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { HomeService } from './home.service';
import { filter, map } from 'rxjs/operators';
import { User } from 'src/app/models/User';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  userDetailsForm: FormGroup;
  firstName: FormControl;
  lastName: FormControl;
  email: FormControl;
  state$: any;
  loggedinUser: User = new User();
  constructor(private homervice: HomeService, 
              private route: ActivatedRoute, 
              private authenticationService:AuthenticationService,
              private router: Router) { }

  ngOnInit(): void {
    if (!this.authenticationService.currentUserValue) { 
      this.router.navigate([""]);
    };
    
    this.route.queryParams.subscribe(params => {
      this.loggedinUser["firstName"] = params["firstName"];
      this.loggedinUser["lastName"] = params["lastName"];
      this.loggedinUser["email"] = params["email"];
      this.loggedinUser["password"] = "";

      this.firstName = new FormControl(this.loggedinUser.firstName, Validators.required);
      this.lastName = new FormControl(this.loggedinUser.lastName, Validators.required);
      this.email = new FormControl(this.loggedinUser.email, [
      Validators.required,
      Validators.email
      ]);
      this.userDetailsForm = new FormGroup({
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email
      })
  });

   
    
  }

  updateUser() {
    let user: User = new User();
    user["firstName"] = this.firstName.value;
    user["lastName"] = this.lastName.value;
    user["email"] = this.email.value;
    // user["password"] = this.pa
    this.homervice.updateUserDetails(user)
    .subscribe(data=>{
      alert("User details Updated Succesfully");
    }, error=>{
        alert("Issue Saving User details");
    })
  }

}
