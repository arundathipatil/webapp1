import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { HomeService } from '../home.service';

import { User } from '../../../models/User';

export interface Book {
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

}

/** Constants used to fill up our data base. */
const COLORS: string[] = [
  'maroon', 'red', 'orange', 'yellow', 'olive', 'green', 'purple', 'fuchsia', 'lime', 'teal',
  'aqua', 'blue', 'navy', 'black', 'gray'
];
const NAMES: string[] = [
  'Maia', 'Asher', 'Olivia', 'Atticus', 'Amelia', 'Jack', 'Charlotte', 'Theodore', 'Isla', 'Oliver',
  'Isabella', 'Jasper', 'Cora', 'Levi', 'Violet', 'Arthur', 'Mia', 'Thomas', 'Elizabeth'
];



@Component({
  selector: 'app-my-book-store',
  templateUrl: './my-book-store.component.html',
  styleUrls: ['./my-book-store.component.scss']
})
export class MyBookStoreComponent {
  currentUser: User = new User();
  bookList: any;
  editedRow: Book;
  displayedColumns: string[] = ['isbn', 'title', 'authors', 'publicationDate', 'quantity', 'price', 'action'];
  dataSource: MatTableDataSource<Book>;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private homeService: HomeService, public dialog: MatDialog) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.homeService.getAllBooksByemail(this.currentUser.email)
      .subscribe((data) => {
        this.bookList = data;
        this.dataSource = new MatTableDataSource(this.bookList);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      })
    const books = [{
      ISBN: "12345",
      title: "Networking",
      authors: "Peter",
      publicationDate: "1/2/2020",
      quantity: 1,
      price: 999,
      action: ""
    }]
  }

  ngOnInit() {

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }


  updateBook(row): void {
    const dialogRef = this.dialog.open(UpdateBookDialogComponent, {
      width: '250px',
      data: row
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.editedRow = result;
      this.editedRow.updatedDate = new Date();
      if(result!=null){
        this.homeService.updateBookDetails(this.editedRow)
        .subscribe(data => {
          alert("Book details updated successfully");
        }, error => {
          alert("Book details could not be saved Successfully");
        })
      }
    });
  }

  deleteBook(row): void {
    const dialogRef = this.dialog.open(DeleteBookDialogComponent, {
      width: '250px',
      data: row
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.editedRow = result;
      this.editedRow.updatedDate = new Date();
      if (result != null) {
        this.homeService.deleteBook(result?.id)
          .subscribe(data => {
            alert("Book deleted successfully");
          }, error => {
            alert("Book could not be deleted Successfully");
          })
      }
    });
  }


}


@Component({
  selector: 'update-book-dialog',
  templateUrl: 'update-book-dialog.html',
})
export class UpdateBookDialogComponent {
  bookForm: FormGroup;
  isbn: FormControl;
  title: FormControl;
  authors: FormControl;
  publicationDate: FormControl;
  quantity: FormControl;
  price: FormControl;

  constructor(
    public dialogRef: MatDialogRef<UpdateBookDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Book) {
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
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}


@Component({
  selector: 'delete-book-dialog',
  templateUrl: 'delete-book-dialog.html',
})
export class DeleteBookDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<UpdateBookDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Book) { }
  onNoClick(): void {
    this.dialogRef.close();
  }
}
