import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { FileUploader } from "ng2-file-upload";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { DomSanitizer } from '@angular/platform-browser';



import { Book } from 'src/app/models/book';
import { HomeService } from '../home.service';
import { Image } from 'src/app/models/image';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit {
  book: Book = new Book();
  navigatedFrom: number;
  uploadForm: FormGroup;
  imageList: any;
  public uploader: FileUploader = new FileUploader({
    isHTML5: true
  });
  title: string = 'Angular File Upload';
  constructor(private fb: FormBuilder,
    private http: HttpClient,
    private homeService: HomeService,
    private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.book = JSON.parse(localStorage.getItem('bookDetails'));
    this.navigatedFrom = parseInt(localStorage.getItem('navigatedFrom'))
    if(this.navigatedFrom == 1) {
      this.uploadForm = this.fb.group({
        document: [null, null]
        // type:  [null, Validators.compose([Validators.required])]
      });    
    }

    this.getImages(this.book);
    
  }

  uploadSubmit() {
    for (let i = 0; i < this.uploader.queue.length; i++) {
      let fileItem = this.uploader.queue[i]._file;
      if (fileItem.size > 10000000) {
        alert("Each File should be less than 10 MB of size.");
        return;
      }
    }
    for (let j = 0; j < this.uploader.queue.length; j++) {
      let data = new FormData();
      let fileItem = this.uploader.queue[j]._file;
      console.log(fileItem.name);
      data.append('file', fileItem);
      data.append('bookId', this.book.isbn);
      data.append('userId', this.book.userEmail);
      this.uploadFile(data);
      // .subscribe(data => alert(data.message));
    }
    this.uploader.clearQueue();
  }

  uploadFile(data: FormData) {
    this.homeService.uploadPhoto(data)
      .subscribe(data => {
        console.log(JSON.stringify(data));
      }, err => {
        alert("issue uploading the photo");
      });
  }

  getImages(book: Book) {
    this.homeService.getImages(book.userEmail, book.isbn)
    .subscribe(data=>{
      console.log(data);
      this.imageList = data;
      this.domSanitizer.bypassSecurityTrustUrl(this.imageList[1]);

    }, error=>{
      alert("Unable to fetch Images");
    })
  }

  deleteImage(image: Image) {
    this.homeService.deleteImage(image.id)
    .subscribe(data=>{
      alert("Image Deleted Successfully");
    }, err=>{
      alert("Issue Deleting the photo");
    })
  }
}
