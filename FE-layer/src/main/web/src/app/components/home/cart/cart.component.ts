import { Component , OnInit, ViewChild, Inject} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, FormControl, EmailValidator } from '@angular/forms';
import { HomeService } from '../home.service';

import { User } from '../../../models/User';


export interface Cart {
  id?: number;
  isbn: string;
  buyersEmail: string;
  quantity: number;
  sellersemail: string;
  title: string;
  price: number;
  action: any;
}

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  currentUser: User = new User();
  cartList: any;
  editedRow: Cart;
  displayedColumns: string[] = ['isbn', 'title', 'sellersemail', 'quantity', 'price', 'action'];
  dataSource: MatTableDataSource<Cart>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private homeService: HomeService, public dialog: MatDialog) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.homeService.getAllCartItems(this.currentUser.email)
    .subscribe((data)=>{
      this.cartList = data;
      this.dataSource = new MatTableDataSource(this.cartList);
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

  deleteItemFromCart(row) {
    this.homeService.deleteItemFromCart(row.id)
    .subscribe(data=>{
      alert("Irem removed from cart successfully");
    }, error=>{
      alert("Item could not be removed from cart");
    })
  }
}
