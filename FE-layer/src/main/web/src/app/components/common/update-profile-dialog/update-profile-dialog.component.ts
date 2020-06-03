import { Component, OnInit, Inject } from '@angular/core';
import { Router, ActivatedRoute, NavigationStart } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { filter, map } from 'rxjs/operators';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { User } from 'src/app/models/User';
import { AuthenticationService } from '../../../services/authentication.service';

@Component({
  selector: 'app-update-profile-dialog',
  templateUrl: './update-profile-dialog.component.html',
  styleUrls: ['./update-profile-dialog.component.scss']
})
export class UpdateProfileDialogComponent implements OnInit {

  userDetailsForm: FormGroup;
  firstName: FormControl;
  lastName: FormControl;
  email: FormControl;
  constructor(public dialogRef: MatDialogRef<UpdateProfileDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: User) { }

  ngOnInit(): void {
    this.firstName = new FormControl('', Validators.required);
    this.lastName = new FormControl('', Validators.required);
    this.email = new FormControl('', [
    Validators.required,
    Validators.email
    ]);
    this.userDetailsForm = new FormGroup({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    })
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
