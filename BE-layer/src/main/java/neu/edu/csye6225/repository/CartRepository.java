package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByBuyersemail(String email);
    Cart findBySellersemailAndIsbn(String email, String isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart c set quantity =?1, price =?2 where c.sellersemail = ?3  AND c.isbn = ?4 AND c.buyersemail = ?5",
            nativeQuery = true)
    void updateCart(int quantity,double price, String sellersemail, String isbn, String buyersemail);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart c set price =?1, title =?2, isbn = ?3  where c.id = ?4",
            nativeQuery = true)
    void updateBookDetailsInCart(double price, String title, String isbn, int id);
}
