package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Book;
import neu.edu.csye6225.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    List<UserSession> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE userSession c set isLoggedIn =?1 where c.email = ?2",
            nativeQuery = true)
    void updateUserSession(boolean isLoggedIn, String email);
}
