package neu.edu.csye6225.service;

import neu.edu.csye6225.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface AmazonService {
    String uploadFile(MultipartFile multipartFile, String bookISBN, String userEmail);
    String getFile(String name);
    public String deleteFile(String name);
}
