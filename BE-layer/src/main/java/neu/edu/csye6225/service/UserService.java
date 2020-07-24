package neu.edu.csye6225.service;

import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<Object> resetPassword(String email) throws Exception;

}
