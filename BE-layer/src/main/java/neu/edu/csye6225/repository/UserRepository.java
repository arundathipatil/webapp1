package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    @Transactional
    @Modifying
    @Query(value = "UPDATE user u set first_Name =?1 , last_Name =?2 where u.email = ?3",
            nativeQuery = true)
    void updateUser(String firstName, String lastName, String email);
}
