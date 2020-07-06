package neu.edu.csye6225.controller;

import neu.edu.csye6225.model.*;
import neu.edu.csye6225.repository.CartRepository;
import neu.edu.csye6225.repository.ImageRepository;
import neu.edu.csye6225.service.AmazonService;
import neu.edu.csye6225.service.AmazonServiceImpl;
import org.springframework.data.domain.Sort;
import neu.edu.csye6225.helper.Hashing;
import neu.edu.csye6225.repository.BookRepository;
import neu.edu.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Autowired
    private AmazonService  amazonService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private StatsDClient stasDClient;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/registerUser")
    public @ResponseBody User registerNewUser(@RequestBody User user) {
        User u = null;
        try {
            logger.debug("this is debug message");
            logger.info("Info:Calling registerNewUserApi");
            stasDClient.incrementCounter("registerNewUSerApi");
            long begin = System.currentTimeMillis();
            u = userRespository.getUserByEmail(user.getEmail());
            long end = System.currentTimeMillis();
            long timeTaken = end-begin;
            logger.info("TIme taken by registerNewUser API " + timeTaken + "ms");
            stasDClient.recordExecutionTime("registerNewUserApiTime", timeTaken);
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

        try {
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
        } catch (Exception e) {
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
                                .filter(a -> a.getQuantity() !=0)
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

    @PostMapping("/uploadPhoto")
    public @ResponseBody String uploadPhoto(@RequestPart(value = "file") MultipartFile multipartFile,@RequestParam(value = "bookId") String bookID, @RequestParam(value = "userId") String userId) {
        return this.amazonService.uploadFile(multipartFile, bookID, userId);
    }

    @GetMapping("/getPhotosByBookISBNAndEmail")
    public List<Image> getPhoto(@RequestParam String userEmail, @RequestParam String isbn) {
        List<String> photoList = new ArrayList<>();

        List<Image> imageMetadata = new ArrayList<>();

        imageMetadata = imageRepository.findAllByIsbnAndUserEmail(isbn, userEmail);

        for (Image img: imageMetadata){
            img.setImage(amazonService.getFile(img.getName()));
        }
        return  imageMetadata;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/deleteImage")
    public boolean deleteImage(@RequestParam int id) {
        try{
            String name = imageRepository.getOne(id).getName();
            String deleted =  amazonService.deleteFile(name);
            if(deleted.equals("SUCCESS")) {
                imageRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
       return false;
    }
}
