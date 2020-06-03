import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { HomeComponent} from './home.component';
import { MyBookStoreComponent } from './my-book-store/my-book-store.component';
import { AddBookComponent } from './add-book/add-book.component';
import { BuyBooksComponent } from './buy-books/buy-books.component';
import { CartComponent } from './cart/cart.component';

const routes: Routes = [
    // {path: '' , component : HomeComponent},
    // {path: 'welcome', component: WelcomeComponent}
    {path: '',  component: HomeComponent,
     children: [{path: 'welcome', component: WelcomeComponent},
                {path: 'myBookStore', component: MyBookStoreComponent},
                {path: 'addBook', component: AddBookComponent},
                {path: 'buyBook', component: BuyBooksComponent},
                {path: 'cart', component: CartComponent}]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
