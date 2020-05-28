import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule} from '@angular/material/toolbar';
import { MatButtonModule} from '@angular/material/button';
import {MatMenuModule} from '@angular/material/menu';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatSliderModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule
  ],
  exports:[
    MatSliderModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule
  ]
})
export class AngularMatModule { }
