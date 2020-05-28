import { Injectable } from '@angular/core';
import { ApiService } from '../../core/api.service';
import { constant } from '../../constant/const';
import { User } from '../../models/User';


@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private apiService: ApiService) { }

  getUserDetails(email: any) {
    return this.apiService.get(constant.urls.getUserDeatils + "?email="+email);
  }

  updateUserDetails(user: User) {
     return this.apiService.post(constant.urls.updateUserDetails, user);
  }
}
