import { Component } from '@angular/core';
import { Router,  } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { User } from './models/User';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'web';
  user: User;
  loggedIn: boolean = false;
  constructor(private authService: AuthenticationService, private router: Router) { 

    if (this.authService.currentUserValue) { 
      this.loggedIn = true;
      this.user = new User();
      this.user = JSON.parse(localStorage.getItem('currentUser'));
    };
  }


  logout() {
    this.authService.logout();
    this.router.navigate([""]);

  }
}
