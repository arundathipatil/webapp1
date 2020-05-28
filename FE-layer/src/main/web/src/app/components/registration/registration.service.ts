import { Injectable } from '@angular/core';
import { ApiService } from 'src/app/core/api.service';
import { User } from 'src/app/models/User';
import { constant} from '../../constant/const'
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private api: ApiService) { }

  registerUser(user: User) {
    return this.api.post(constant.urls.register, user);
    // return of([{name:"sucess"}]);
  }
}
