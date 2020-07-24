package neu.edu.csye6225.service;

import neu.edu.csye6225.aws.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {



    @Autowired
    private AmazonSQSClient amazonSQSClient ;
//            = new AmazonSQSClient();

    @Override
    public ResponseEntity<Object> resetPassword(String email) throws Exception {
        try{
            String token = UUID.randomUUID().toString();
            amazonSQSClient.sendMessageToQueue(email, token);
            return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
        } catch (Exception er) {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}
