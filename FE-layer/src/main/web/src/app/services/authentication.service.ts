import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { User } from '../models/User';
import { ApiService } from '../core/api.service';
import { constant } from '../constant/const'

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;
    public isloggedIn: BehaviorSubject<boolean>;

    constructor(private http: HttpClient, private api: ApiService) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));

        this.currentUser = this.currentUserSubject.asObservable();

        this.isloggedIn = new BehaviorSubject<boolean>(false);
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    login(user: User) {
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    this.currentUserSubject.next(user);
                    this.isloggedIn.next(true);
    }

    logout(email) {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.logoutBackendSession(email).subscribe(data=>{
          console.log("loggedout");
        }, err=>{
          console.log("erloggedout");
        })
        this.currentUserSubject.next(null);
        this.isloggedIn.next(false);
    }

    logoutBackendSession(email: string) {
        return this.api.get(constant.urls.logout+"?email="+email);
    }
}
