package neu.edu.csye6225.aws;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;

@Service("amazonSQSService")
@EnableScheduling
public class AmazonSQSClient {


    private static final Logger logger = LoggerFactory.getLogger(AmazonSQSClient.class);

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private AmazonSQS amazonSQSClient;

    @Value("${QUEUE}")
    private String QUEUE;

//    @Value("${DOMAIN_NAME}")
//    private String domainName;

    @PostConstruct
    private void init() {
        this.amazonSQSClient = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
    }

    public void sendMessageToQueue(String userEmail, String token) {
        try {
            receiveMessageAndDelete();
            String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
            logger.info("AmazonSQSClientClass queueUrl : " + queueUrl + " ----");
            StringBuilder messageString = new StringBuilder();
            messageString.append(userEmail + ",");
            messageString.append("http://" + "prod.arundathipatil.me" + "/reset?email="+ userEmail + "&token=" + token);
            messageString.append(",");
            logger.info("AmazonSQSClientClass- Pushed message to queue with email : " + userEmail + " and token : " + messageString.toString() + "-----");
            SendMessageRequest messageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl).withMessageBody(messageString.toString());
            logger.info("AmazonSQSClientClass-messageRequest "+ messageRequest + "------");
            amazonSQSClient.sendMessage(messageRequest);
            logger.info("AmazonSQSClientClass-Message added in queue ------");
            receiveMessageAndDelete();
        } catch (AmazonSQSException exception) {
            if (!exception.getErrorCode().equals("The queue already exists ------" )) {
                logger.error(exception.getMessage());
                throw exception;
            }
        }
    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void receiveMessageAndDelete() {
        logger.info("AmazonSQSClientClass-Inside receiveMessageAndDelete");
        try{
            String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
            logger.info("AmazonSQSClientClass queueUrl : " + queueUrl + " ----");
            List<Message> receivedMessageList = amazonSQSClient.receiveMessage(queueUrl).getMessages();
            logger.info("AmazonSQSClientClass- Received Message List: "+ receivedMessageList.toString() + "====");
            for(Message message : receivedMessageList) {
                logger.info("AmazonSQSClientClass Inside For loop with meaage: " + message.toString());
                if (message.getBody()!= null && !message.getBody().isEmpty()) {
                    logger.info("AmazonSQSClientClass-Receiving message" + message.getBody() + "==");
                    amazonSNSClient.publish(message.getBody());
                    amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
                }
            }
        } catch (Exception ex) {
            logger.info("AmazonSQSClientClass- Failed in receiveMessageAndDelete :" + ex.getMessage());
            logger.error("AmazonSQSClientClass- Failed in receiveMessageAndDelete :" + ex.getMessage());
        }

    }
}
