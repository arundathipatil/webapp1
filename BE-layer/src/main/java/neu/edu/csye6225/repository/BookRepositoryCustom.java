package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Book;

import java.util.List;

public interface BookRepositoryCustom {
    public List<Book> getAllBooksToBuy(String email);
}
