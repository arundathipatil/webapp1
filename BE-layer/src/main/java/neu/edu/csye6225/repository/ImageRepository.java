package neu.edu.csye6225.repository;

import neu.edu.csye6225.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByIsbnAndUserEmail(String isbn, String userEmail);
}
