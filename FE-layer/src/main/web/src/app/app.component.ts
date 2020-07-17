import { Component } from '@angular/core';
import { Router,  } from '@angular/router';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { AuthenticationService } from './services/authentication.service';
import { User } from './models/User';
import { UpdateProfileDialogComponent } from './components/common/update-profile-dialog/update-profile-dialog.component';
import { HomeService } from './components/home/home.service';
import { ChangePWD } from './models/changepwd';
import { ChangePasswordDialogComponent } from './components/common/change-password-dialog/change-password-dialog.component';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'web';
  user: User;
  loggedIn: boolean = false;
  constructor(private authService: AuthenticationService, 
            private router: Router, 
            public dialog: MatDialog,
            private homeService: HomeService) {
    this.authService.isloggedIn.subscribe(data=>{
      this.loggedIn = data;
    });
    if (this.authService.currentUserValue) { 
      this.loggedIn = true;
      this.user = new User();
      this.user = JSON.parse(localStorage.getItem('currentUser'));
    } else{
      this.loggedIn = false;
    };
  }


  logout() {
    this.user = JSON.parse(localStorage.getItem('currentUser'));
    this.authService.logout(this.user.email);
    // this.authService.logoutBackendSession(this.user.email)
    // .subscribe(data=>{
    //   console.log("loggedout");
    // }, err=>{
    //   console.log("loggedouter");
    // })
    this.router.navigate([""]);
  }

  updateUserDeatils(): void {
    this.user = new User();
    this.user = JSON.parse(localStorage.getItem('currentUser'));
    const dialogRef = this.dialog.open(UpdateProfileDialogComponent, {
      width: '250px',
      data: this.user
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if(result!=null) {
        this.homeService.updateUserDetails(result)
        .subscribe(data=>{
          alert("User details updated successfully");
        }, error=>{
          alert("User details could not be saved Successfully");
        });
      }      
    });
  }

  changePassword() {
    this.user = new User();
    this.user = JSON.parse(localStorage.getItem('currentUser'));
    let pwddata : ChangePWD = new ChangePWD();

    pwddata.newPassword = "";
    pwddata.currentPassword = "";
    pwddata.email = "";

    const dialogRef = this.dialog.open(ChangePasswordDialogComponent, {
      width: '250px',
      data: pwddata
    });

    dialogRef.afterClosed().subscribe(result => {
      result.email = this.user.email;
      if(result!=null) {
        this.homeService.changePassword(result)
        .subscribe(data=>{
          alert("Password updated successfully");
        }, error=>{
          alert("Password could not be updated Successfully");
        });
      }      
    });

  }

  
}
