import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject , of} from 'rxjs';
import { ApiService } from '../../core/api.service';
import { constant } from '../../constant/const';
import { User } from '../../models/User'

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private userDetails: BehaviorSubject<User>;

  constructor(private apiService: ApiService) {
    let user: User = null;
    this.userDetails = new BehaviorSubject<User>(user);
   }

  login(email: string, pwd: string) : Observable<any> {
    // return this.apiService.get(constant.urls.login +"?email="+email+"&pwd="+pwd);
    return of([{name:"sucess"}]);
  }

  // not needed
  setUser(u : User) : void {
    this.userDetails.next(u);
  }
  
  // not needed
  getUser(): Observable<User> {
    return this.userDetails.asObservable();
  }

  // not needed
  getUserDeatislFromApi(email: string) {
     return this.apiService.get(constant.urls.getUserDeatils + "?email="+email);
  }
}
