import { Injectable } from '@angular/core';
import { ApiService } from '../../core/api.service';
import { constant } from '../../constant/const';
import { User } from '../../models/User';
import { Book } from '../../models/book';
import { Cart } from '../../models/cart';


@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private apiService: ApiService) { }

  getUserDetails(email: any) {
    return this.apiService.get(constant.urls.getUserDeatils + "?email="+email);
  }

  updateUserDetails(user: User) {
     return this.apiService.post(constant.urls.updateUserDetails, user);
  }

  addBook(book: Book) {
    return this.apiService.post(constant.urls.addBook, book);
  }

  getAllBooksByemail(email: string) {
    return this.apiService.get(constant.urls.getAllBooksByemail+ "?email="+email)
  }

  updateBookDetails(book: Book) {
    return this.apiService.post(constant.urls.updateBookDetails, book);
  }

  deleteBook(id: number) {
    return this.apiService.delete(constant.urls.deleteBook + "?id="+id);
  }

  getAllBooksToBuy(email: string) {
    return this.apiService.get(constant.urls.getAllBooksToBuy + "?email=" +email);
  }

  addItemToCart(cart: Cart) {
    return this.apiService.post(constant.urls.addItemToCart, cart);
  }

  deleteItemFromCart(id: number) {
    return this.apiService.delete(constant.urls.deleteItemFromCart +"?id="+id);
  }

  getAllCartItems(email: string) {
    return this.apiService.get(constant.urls.getCartItems +"?email="+email);
  }

  changePassword(data: any) {
    return this.apiService.post(constant.urls.changepwd, data);
  }

  uploadPhoto(data: any) {
    return this.apiService.post(constant.urls.uploadPhoto, data);
  }

  getImages(userEmail: string, isbn: string) {
    return this.apiService.get(constant.urls.getImages+"?userEmail="+userEmail+"&isbn="+isbn);
  }

  deleteImage(id: number) {
    return this.apiService.delete(constant.urls.deleteImage+"?id="+id);
  }
}
