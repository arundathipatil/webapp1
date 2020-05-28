package neu.edu.csye6225.controller;

import neu.edu.csye6225.helper.Hashing;
import neu.edu.csye6225.model.User;
import neu.edu.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRespository;

    @PostMapping("/registerUser")
    public @ResponseBody String registerNewUser(@RequestBody User user) {
//        if(this.getUserDetails(user.getEmail()) !=null) {
            String hashedVal = Hashing.hashPassword(user.getPassword());
            user.setPassword(hashedVal);
            userRespository.save(user);
            return "Saved";
//        } else {
//            return "User email already Exists";
//        }
    }

    @PostMapping("/updateUser")
    public @ResponseBody int updateUserDetails(@RequestBody User user) {
//       int rowsaffected = userRespository.updateUserDetails(user);
        userRespository.updateUser(user.getFirstName(), user.getLastName(), user.getEmail());
        return 1;
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
}
