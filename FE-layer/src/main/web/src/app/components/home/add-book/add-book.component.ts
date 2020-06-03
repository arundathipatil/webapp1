import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Book } from '../../../models/book';
import { User } from '../../../models/User';
import { HomeService } from '../home.service';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent implements OnInit {

  bookForm: FormGroup;
  isbn: FormControl;
  title: FormControl;
  authors: FormControl;
  publicationDate: FormControl;
  quantity: FormControl;
  price: FormControl;
  currentUser: User = new User();


  constructor(private homeService: HomeService) {
    this.isbn = new FormControl('', Validators.required);
    this.title = new FormControl('', Validators.required);
    this.authors = new FormControl('', Validators.required);
    this.publicationDate = new FormControl('', Validators.required);
    this.quantity = new FormControl('', [Validators.required, Validators.min(0), Validators.max(999)]);
    this.price = new FormControl('', [Validators.required, Validators.min(0.01), Validators.max(9999.99)]);

    this.bookForm = new FormGroup({
      isbn: this.isbn,
      title: this.title,
      authors: this.authors,
      publicationDate: this.publicationDate,
      quantity: this.quantity,
      price: this.price
    });

      this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
   }

  ngOnInit(): void {
  }

  addBook()  {
    let book = new Book();
    book.isbn = this.isbn.value;
    book.title = this.title.value;
    book.authors = this.authors.value;
    book.price = this.price.value;
    book.publicationDate = this.publicationDate.value;
    book.quantity = this.quantity.value;
    book.createdDate = new Date();
    book.updatedDate = null;
    book.userEmail = this.currentUser.email;


    this.homeService.addBook(book)
    .subscribe(data=>{
        alert("Book Saved!");
    }, error=>{
      alert("some issue");
    })
  }
}
