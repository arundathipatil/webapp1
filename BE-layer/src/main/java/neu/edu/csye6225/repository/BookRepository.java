package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Book;
import neu.edu.csye6225.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @EntityGraph(attributePaths = "bookCategory")
    List<Book> findFirst10ByOrderByTitleAsc();

    Book findByisbnAndUserEmail(String ISBN, String email);
    List<Book> getAllByUserEmail(String email);
    Book findById(int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE book u set authors =?1 , price =?2, publication_date =?3, quantity = ?4, title = ?5, updated_date = ?6, isbn =?7  where u.user_email  = ?8 AND u.id = ?9",
            nativeQuery = true)
    void updateBookDetails(String authors, double price, Date publicationDate, int quantity, String title, Date updatedDate, String isbn, String userEmail, int id);

    void deleteByIdAndUserEmail(int id, String email);
}
