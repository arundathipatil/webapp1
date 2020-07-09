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
        logger.info("Info:Calling registerNewUserApi");
        long begin = System.currentTimeMillis();
        stasDClient.incrementCounter("registerNewUSerApi");
        User u = null;
        try {
            long beginDB = System.currentTimeMillis();
            u = userRespository.getUserByEmail(user.getEmail());
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            long timeTakenByDBQuery = end - beginDB;
            stasDClient.recordExecutionTime("DB-registerNewUserDBQueryTime", timeTakenByDBQuery);
            logger.info("TIme taken by registerNewUser API " + timeTaken + "ms");
            stasDClient.recordExecutionTime("registerNewUserApiTime", timeTaken);
        } catch (Exception e) {
            String hashedVal = Hashing.hashPassword(user.getPassword());
            user.setPassword(hashedVal);
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by registerNewUser API " + timeTaken + "ms");
            stasDClient.recordExecutionTime("registerNewUserApiTime", timeTaken);
            long beginDB = System.currentTimeMillis();
            User user1 = userRespository.save(user);
            long endDB = System.currentTimeMillis();
            long timeTakenByDBQuery = endDB - beginDB;
            stasDClient.recordExecutionTime("DB-registerNewUserDBQueryTime", timeTakenByDBQuery);
//            return userRespository.save(user);
            return user1;
        }
        return null;
    }

    @PostMapping("/updateUser")
    public @ResponseBody int updateUserDetails(@RequestBody User user) {
        logger.info("Info:Calling updateUserApi");
        stasDClient.incrementCounter("updateUserApi");
        long begin = System.currentTimeMillis();
        userRespository.updateUser(user.getFirstName(), user.getLastName(), user.getEmail());
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by updateUserApi API " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-updateUserApiTime", timeTaken);
        return 1;
    }

    @PostMapping("/changePassword")
    public @ResponseBody boolean changePassword(@RequestBody Changepwd pwd) {
        User u = null;
        logger.info("Info:Calling changePasswordApi");
        stasDClient.incrementCounter("changePasswordApi");
        long begin = System.currentTimeMillis();
        try {
            u = userRespository.getUserByEmail(pwd.getEmail());
            System.out.println(u.getPassword());
            if(u != null && u.getPassword().equals(Hashing.hashPassword(pwd.getCurrentPassword())))  {
                String hashedValPWD = Hashing.hashPassword(pwd.getNewPassword());
                userRespository.changePassword(hashedValPWD, pwd.getEmail());
                long end = System.currentTimeMillis();
                long timeTaken = end - begin;
                logger.info("TIme taken by changePasswordApi API " + timeTaken + "ms");
                stasDClient.recordExecutionTime("DB-changePasswordApiTime", timeTaken);
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
        logger.info("Info:Calling userDetailsApi");
        stasDClient.incrementCounter("userDetailsApi");
        long begin = System.currentTimeMillis();
        User u = null;
        try {
           u =  userRespository.getUserByEmail(email);
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by userDetailsApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("DB-userDetailsApiTime", timeTaken);
           return u;
        } catch (Exception e) {
            u.setLastName(e.getMessage());
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by userDetails API " + timeTaken + "ms");
            stasDClient.recordExecutionTime("DB-userDetailsApiTime", timeTaken);
            return u;
        }
    }

    @GetMapping("/login")
    public boolean login(@RequestParam String email, @RequestParam String pwd) {
        User u = null;
        logger.info("Info:Calling loginApi");
        stasDClient.incrementCounter("loginApi");
        long begin = System.currentTimeMillis();
        try {
          System.out.println(userRespository.getUserByEmail(email));
          u = userRespository.getUserByEmail(email);
          System.out.println(u.getPassword());
          if(u != null && u.getPassword().equals(Hashing.hashPassword(pwd)))  {
              long end = System.currentTimeMillis();
              long timeTaken = end - begin;
              logger.info("TIme taken by loginApi " + timeTaken + "ms");
              stasDClient.recordExecutionTime("loginApiTime", timeTaken);
              return true;
          } else {
              long end = System.currentTimeMillis();
              long timeTaken = end - begin;
              logger.info("TIme taken by loginApi " + timeTaken + "ms");
              stasDClient.recordExecutionTime("loginApiTime", timeTaken);
              return false;
          }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by loginApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("loginApiTime", timeTaken);
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
        logger.info("Info:Calling addBookApi");
        stasDClient.incrementCounter("addBookApi");
        long begin = System.currentTimeMillis();
        Book b = bookRepository.findByisbnAndUserEmail(book.getIsbn(), book.getUserEmail());
        if(b !=null) {
            String error = "Book already exists. Try editing the existing book";
            throw new Exception(error);
        };
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by addBookApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-addBookApiTime", timeTaken);
        return bookRepository.save(book);
    }

    @GetMapping(path="/getAllBooksByEmail")
    public List<Book> getAllBooksByEmail(@RequestParam String email) {
        logger.info("Info:Calling getAllBooksByEmailApi");
        stasDClient.incrementCounter("getAllBooksByEmailApi");
        long begin = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by getAllBooksByEmailApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-getAllBooksByEmailApiTime", timeTaken);

        return bookRepository.getAllByUserEmail(email);
    }

    @PostMapping("/updateBookDetails")
    public @ResponseBody int updateBookDetails(@RequestBody Book book) {
        Book b = bookRepository.findById(book.getId());
        logger.info("Info:Calling updateBookDetailsApi");
        stasDClient.incrementCounter("updateBookDetailsApi");
        long begin = System.currentTimeMillis();
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
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by updateBookDetailsApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("DB-updateBookDetailsApiTime", timeTaken);

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
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by updateBookDetailsApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("DB-updateBookDetailsApiTime", timeTaken);

            return 1;
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/delete")
    public void deleteBook(@RequestParam int id) {
//        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        logger.info("Info:Calling deleteBookApi");
        stasDClient.incrementCounter("deleteBookApi");
        long begin = System.currentTimeMillis();
        Book bookToDelete = bookRepository.findById(id);
        Cart cart = cartRepository.findBySellersemailAndIsbn(bookToDelete.getUserEmail(), bookToDelete.getIsbn());
       if(cart !=null) { cartRepository.deleteById(cart.getId()); };
        bookRepository.deleteById(id);
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by deleteBookApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-deleteBookApiTime", timeTaken);
    }

    @GetMapping("/getBooksToBuy")
    public @ResponseBody List<Book> getBooksToBy(@RequestParam String email) {
        logger.info("Info:Calling getBooksToBuyApi");
        stasDClient.incrementCounter("getBooksToBuyApi");
        long begin = System.currentTimeMillis();

        Comparator<Book> compareByNameAndPrice = Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice);

        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by getBooksToBuyApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-getBooksToBuyApiTime", timeTaken);
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
        logger.info("Info:Calling addBookToCartApi");
        stasDClient.incrementCounter("addBookToCartApi");
        long begin = System.currentTimeMillis();
        Cart cartItem = cartRepository.findBySellersemailAndIsbn(cart.getSellersemail(), cart.getIsbn());
        if(cartItem == null) {
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by addBookToCartApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("addBookToCartApiTime", timeTaken);
            return cartRepository.save(cart);
        } else {
            cartRepository.updateCart(cart.getQuantity(), cart.getPrice(), cart.getSellersemail(), cart.getIsbn(), cart.getBuyersemail());
            Cart item= new Cart();
            long end = System.currentTimeMillis();
            long timeTaken = end - begin;
            logger.info("TIme taken by addBookToCartApi " + timeTaken + "ms");
            stasDClient.recordExecutionTime("DB-addBookToCartApiTime", timeTaken);
            return item;
        }
    }


    @GetMapping("/getCartItems")
    public @ResponseBody List<Cart> getCartItems(@RequestParam String email) {
        logger.info("Info:Calling getCartItemsApi");
        stasDClient.incrementCounter("getCartItemsApi");
        long begin = System.currentTimeMillis();
       List<Book> booksInCart = new ArrayList<>();
       List<Cart> cartList = cartRepository.findAllByBuyersemail(email);

        for (Cart cartItem : cartList) {
            booksInCart.add(bookRepository.findByisbnAndUserEmail(cartItem.getIsbn(), cartItem.getBuyersemail()));
        }
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by getCartItemsApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-getCartItemsApiTime", timeTaken);
        return cartList;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/deleteItemFromCart")
    public void deleteCartItem(@RequestParam int id) {
        logger.info("Info:Calling deleteItemFromCartApi");
        stasDClient.incrementCounter("deleteItemFromCartApi");
        long begin = System.currentTimeMillis();
        cartRepository.deleteById(id);
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by deleteItemFromCartApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("DB-deleteItemFromCartApiTime", timeTaken);
    }

    @PostMapping("/uploadPhoto")
    public @ResponseBody String uploadPhoto(@RequestPart(value = "file") MultipartFile multipartFile,@RequestParam(value = "bookId") String bookID, @RequestParam(value = "userId") String userId) {
        logger.info("Info:Calling uploadPhototoS3");
        stasDClient.incrementCounter("uploadPhototoS3");
        long begin = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by uploadPhototoS3 API " + timeTaken + "ms");
        stasDClient.recordExecutionTime("uploadPhototoS3", timeTaken);
        return this.amazonService.uploadFile(multipartFile, bookID, userId);
    }

    @GetMapping("/getPhotosByBookISBNAndEmail")
    public List<Image> getPhoto(@RequestParam String userEmail, @RequestParam String isbn) {
        logger.info("Info:Calling bookViewed-getPhotosByBookISBNAndEmailAPI");
        stasDClient.incrementCounter("bookViewed-getPhotosByBookISBNAndEmailAPI");
        long begin = System.currentTimeMillis();
        List<String> photoList = new ArrayList<>();

        List<Image> imageMetadata = new ArrayList<>();

        imageMetadata = imageRepository.findAllByIsbnAndUserEmail(isbn, userEmail);

        for (Image img: imageMetadata){
            img.setImage(amazonService.getFile(img.getName()));
        }
        long end = System.currentTimeMillis();
        long timeTaken = end - begin;
        logger.info("TIme taken by bookViewed-getPhotosByBookISBNAndEmailApi " + timeTaken + "ms");
        stasDClient.recordExecutionTime("S3bookViewed-getPhotosByBookISBNAndEmailTime", timeTaken);
        return  imageMetadata;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/deleteImage")
    public boolean deleteImage(@RequestParam int id) {
        logger.info("Info:Calling deleteImageApi");
        stasDClient.incrementCounter("deleteImageApi");
        long begin = System.currentTimeMillis();
        try{
            String name = imageRepository.getOne(id).getName();
            String deleted =  amazonService.deleteFile(name);
            if(deleted.equals("SUCCESS")) {
                imageRepository.deleteById(id);
                long end = System.currentTimeMillis();
                long timeTaken = end - begin;
                logger.info("TIme taken by deleteImageApi " + timeTaken + "ms");
                stasDClient.recordExecutionTime("S3deleteImageApiTime", timeTaken);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
       return false;
    }
}
