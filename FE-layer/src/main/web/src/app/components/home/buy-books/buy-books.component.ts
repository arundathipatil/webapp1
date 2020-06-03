import { Component , OnInit, ViewChild, Inject} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, FormControl, EmailValidator } from '@angular/forms';
import { HomeService } from '../home.service';
import { User } from '../../../models/User';
import { Cart } from '../../../models/cart';

export interface Book{
  isbn: string;
  title: string;
  authors: string;
  publicationDate: Date;
  quantity: number;
  price: number;
  action: string;
  createdDate: Date;
  updatedDate: Date;
  id: number;
  userEmail: string;
  selectedQuanity: number;
}


@Component({
  selector: 'app-buy-books',
  templateUrl: './buy-books.component.html',
  styleUrls: ['./buy-books.component.scss']
})
export class BuyBooksComponent implements OnInit {
  currentUser: User = new User();
  bookList: any;
  editedRow: Book;
  displayedColumns: string[] = ['isbn', 'title', 'price', 'authors', 'publicationDate', 'quantity','userEmail', 'action'];
  dataSource: MatTableDataSource<Book>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private homeService: HomeService, public dialog: MatDialog) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.homeService.getAllBooksToBuy(this.currentUser.email)
    .subscribe((data)=>{
      this.bookList = data;
      this.dataSource = new MatTableDataSource(this.bookList);
      this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    })
   }

  ngOnInit(): void {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addToCart(row) {
    let cart : Cart = new Cart();
    cart.isbn = row.isbn;
    cart.sellersemail = row.userEmail;
    cart.buyersemail = this.currentUser.email;
    cart.quantity = parseInt(row.selectedQuanity);
    cart.title = row.title;
    cart.price = row.price;

    this.homeService.addItemToCart(cart)
    .subscribe(data=>{
      alert("Item added to cart");
    }, error=>{
      alert("Item could not be added successfully");
    });
  }

}
