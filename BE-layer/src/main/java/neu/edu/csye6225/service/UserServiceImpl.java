package neu.edu.csye6225.service;

import neu.edu.csye6225.aws.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {



    @Autowired(required = false)
    private AmazonSQSClient amazonSQSClient;

    @Override
    public ResponseEntity<Object> resetPassword(String email) {
        amazonSQSClient.sendMessageToQueue(email);
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }
}
