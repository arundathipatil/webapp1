package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.User;

public interface UserRepositoryCustom {
    public User getUserByEmail(String email);
    public int updateUserDetails(User user);
}
