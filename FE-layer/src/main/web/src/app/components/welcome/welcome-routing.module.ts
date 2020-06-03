import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WelcomeUserComponent } from './welcome-user/welcome-user.component';


const routes: Routes = [
    {path: '' , component : WelcomeUserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule { }
