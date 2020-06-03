import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule} from '@angular/material/toolbar';
import { MatButtonModule} from '@angular/material/button';
import {MatMenuModule} from '@angular/material/menu';
import {MatListModule} from '@angular/material/list'; 
import {MatTableModule} from '@angular/material/table'; 
import {MatSortModule} from '@angular/material/sort';
import { MatFormFieldModule} from '@angular/material/form-field'
import {MatInputModule} from '@angular/material/input'

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatSliderModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatListModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule
  ],
  exports:[
    MatSliderModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatListModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule
  ]
})
export class AngularMatModule { }
