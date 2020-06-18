import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { MyBookStoreComponent,UpdateBookDialogComponent, DeleteBookDialogComponent } from './my-book-store/my-book-store.component';
import { WelcomeComponent } from './welcome/welcome.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import {FileUploadModule} from "ng2-file-upload";
import { AddBookComponent } from './add-book/add-book.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { BuyBooksComponent } from './buy-books/buy-books.component';
import { CartComponent } from './cart/cart.component';
import { BookDetailsComponent } from './book-details/book-details.component';




@NgModule({
  declarations: [
    HomeComponent, 
    MyBookStoreComponent, 
    WelcomeComponent, 
    AddBookComponent, 
    DeleteBookDialogComponent, 
    UpdateBookDialogComponent, 
    BuyBooksComponent, 
    CartComponent, 
    BookDetailsComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatTableModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatInputModule,
    ReactiveFormsModule,
    FormsModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MDBBootstrapModule,
    FileUploadModule
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ]
})
export class HomeModule { }
