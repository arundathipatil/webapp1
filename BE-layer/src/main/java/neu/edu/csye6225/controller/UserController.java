package neu.edu.csye6225.controller;

import neu.edu.csye6225.model.Cart;
import neu.edu.csye6225.model.Changepwd;
import neu.edu.csye6225.repository.CartRepository;
import org.springframework.data.domain.Sort;
import neu.edu.csye6225.helper.Hashing;
import neu.edu.csye6225.model.Book;
import neu.edu.csye6225.model.User;
import neu.edu.csye6225.repository.BookRepository;
import neu.edu.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRespository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/registerUser")
    public @ResponseBody User registerNewUser(@RequestBody User user) {
        User u = null;
        try {
            u = userRespository.getUserByEmail(user.getEmail());
        } catch (Exception e) {
            String hashedVal = Hashing.hashPassword(user.getPassword());
            user.setPassword(hashedVal);
            return userRespository.save(user);
        }
        return null;
    }

    @PostMapping("/updateUser")
    public @ResponseBody int updateUserDetails(@RequestBody User user) {
        userRespository.updateUser(user.getFirstName(), user.getLastName(), user.getEmail());
        return 1;
    }

    @PostMapping("/changePassword")
    public @ResponseBody boolean changePassword(@RequestBody Changepwd pwd) {
        User u = null;
        try {
            u = userRespository.getUserByEmail(pwd.getEmail());
            System.out.println(u.getPassword());
            if(u != null && u.getPassword().equals(Hashing.hashPassword(pwd.getCurrentPassword())))  {
                String hashedValPWD = Hashing.hashPassword(pwd.getNewPassword());
                userRespository.changePassword(hashedValPWD, pwd.getEmail());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            u.setFirstName(e.getMessage());
            return false;
        }
    }

    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRespository.findAll();
    }

    @GetMapping("/userDetails")
    public User getUserDetails(@RequestParam String email) {
        User u = null;
        try {
           u =  userRespository.getUserByEmail(email);
           return u;
        } catch (Exception e) {
            u.setLastName(e.getMessage());
            return u;
        }
    }

    @GetMapping("/login")
    public boolean login(@RequestParam String email, @RequestParam String pwd) {
        User u = null;
        try {
          System.out.println(userRespository.getUserByEmail(email));
          u = userRespository.getUserByEmail(email);
          System.out.println(u.getPassword());
          if(u != null && u.getPassword().equals(Hashing.hashPassword(pwd)))  {
              return true;
          } else {
              return false;
          }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            u.setFirstName(e.getMessage());
            return false;
        }
    }

    /**
     * Book related APIS's
     */
    @PostMapping("/addBook")
    @CrossOrigin(origins = "http://localhost:4200")
    public Book addNewBook(@RequestBody Book book) throws Exception {
        //        Book b = new Book();
        //        book.s
        //        String email = book.getUser().getEmail();
        //        User user = this.getUserDetails(email);
        //        book.setUser(getUserDetails(email));
        Book b = bookRepository.findByisbnAndUserEmail(book.getIsbn(), book.getUserEmail());
        if(b !=null) {
            String error = "Book already exists. Try editing the existing book";
            throw new Exception(error);
        };
        return bookRepository.save(book);
    }

    @GetMapping(path="/getAllBooksByEmail")
    public List<Book> getAllBooksByEmail(@RequestParam String email) {
        return bookRepository.getAllByUserEmail(email);
    }

    @PostMapping("/updateBookDetails")
    public @ResponseBody int updateBookDetails(@RequestBody Book book) {
        Book b = bookRepository.findById(book.getId());
        Cart c = cartRepository.findBySellersemailAndIsbn(book.getUserEmail(), book.getIsbn());
        int cartId = c.getId();
        cartRepository.updateBookDetailsInCart(book.getPrice(), book.getTitle(), book.getIsbn(), cartId);
        bookRepository.updateBookDetails(book.getAuthors(),
                                    book.getPrice(),
                                    book.getPublicationDate(),
                                    book.getQuantity(),
                                    book.getTitle(),
                                    book.getUpdatedDate(),
                                    book.getIsbn(),
                                    book.getUserEmail(),
                                    book.getId());

        return 1;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/delete")
    public void deleteBook(@RequestParam int id) {
//        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        Book bookToDelete = bookRepository.findById(id);
        Cart cart = cartRepository.findBySellersemailAndIsbn(bookToDelete.getUserEmail(), bookToDelete.getIsbn());
       if(cart !=null) { cartRepository.deleteById(cart.getId()); };
        bookRepository.deleteById(id);
    }

    @GetMapping("/getBooksToBuy")
    public @ResponseBody List<Book> getBooksToBy(@RequestParam String email) {

        Comparator<Book> compareByNameAndPrice = Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice);

      return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"))
                                .stream()
                                .filter(a-> !a.getUserEmail().equals(email))
                                .sorted(compareByNameAndPrice)
                                .collect(Collectors.toList());

    }


    /**
     * Cart APIs
     */
    @PostMapping("/addBookToCart")
    public @ResponseBody Cart addBookToCart(@RequestBody Cart cart) {
        Cart cartItem = cartRepository.findBySellersemailAndIsbn(cart.getSellersemail(), cart.getIsbn());
        if(cartItem == null) {
            return cartRepository.save(cart);
        } else {
            cartRepository.updateCart(cart.getQuantity(), cart.getPrice(), cart.getSellersemail(), cart.getIsbn(), cart.getBuyersemail());
            Cart item= new Cart();
            return item;
        }
    }


    @GetMapping("/getCartItems")
    public @ResponseBody List<Cart> getCartItems(@RequestParam String email) {
       List<Book> booksInCart = new ArrayList<>();
       List<Cart> cartList = cartRepository.findAllByBuyersemail(email);

        for (Cart cartItem : cartList) {
            booksInCart.add(bookRepository.findByisbnAndUserEmail(cartItem.getIsbn(), cartItem.getBuyersemail()));
        }
        return cartList;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/deleteItemFromCart")
    public void deleteCartItem(@RequestParam int id) {
        cartRepository.deleteById(id);
    }
}
