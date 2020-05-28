package neu.edu.csye6225.repository;

import java.util.*;
import java.util.stream.*;
import neu.edu.csye6225.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    public User getUserByEmail(String email) {
        Query query = entityManager.createNativeQuery("SELECT * FROM user  WHERE email = :email", User.class);

        query.setParameter("email", email);
        System.out.println(query.getSingleResult());
        User user = (User) query.getResultList()
                        .stream().findFirst().orElse(null);
        return user;
    }

    @Override
    @Transactional
    public int updateUserDetails(User user) {

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();

        int rowsAffected = 0;
        Query query = entityManager.createNativeQuery("UPDATE user SET firstName= :firstName, lastName= :lastName  WHERE email = :email", User.class);



        query=  entityManager.createNativeQuery("UPDATE user u"
                        + "SET u.firstName = :firstName "
                        + "WHERE u.email = :email ");

        rowsAffected = query.executeUpdate();
        return rowsAffected;
    }
}
