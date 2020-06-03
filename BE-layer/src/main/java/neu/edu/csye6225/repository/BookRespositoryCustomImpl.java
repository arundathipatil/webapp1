package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Book;
import neu.edu.csye6225.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class BookRespositoryCustomImpl implements BookRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Book> getAllBooksToBuy(String email) {
        Query query = entityManager.createNativeQuery("SELECT * FROM book  WHERE user_email <> :email", Book.class);

       query.setParameter("email", email);
        System.out.println(query.getSingleResult());
        List<Book> books = (List<Book>) query.getResultList();
        return books;
    }
}
