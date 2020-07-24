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

@Service("amazonSQSService")
public class AmazonSQSClient {

    private AmazonSQS amazonSQSClient;
    private final static String QUEUE = "password-reset-queue";
    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private Logger logger = LoggerFactory.getLogger(AmazonSQSClient.class);


    @PostConstruct
    private void init() {
        this.amazonSQSClient = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).
                withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
    }


    public void sendMessageToQueue(String email) {
//        try {
//            receiveMessageAndDeleteFromQueue();
//            CreateQueueResult create_result = amazonSQSClient.createQueue(QUEUE);
//            String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
//            StringBuilder messageString = new StringBuilder();
//            messageString.append(email);
//
//            SendMessageRequest messageRequest = new SendMessageRequest()
//                    .withQueueUrl(queueUrl).withMessageBody(messageString.toString());
//            amazonSQSClient.sendMessage(messageRequest);
//            receiveMessageAndDeleteFromQueue();
//        } catch (AmazonSQSException exception) {
//            if (!exception.getErrorCode().equals("The queue already exists" )) {
//                logger.error(exception.getMessage());
//                throw exception;
//            }
//        }

        try {
            receiveMessageAndDeleteFromQueue();
            String token = UUID.randomUUID().toString();
            CreateQueueResult create_result = amazonSQSClient.createQueue(QUEUE);
            String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
            StringBuilder messageString = new StringBuilder();
            messageString.append(email + ",");
            messageString.append("http://" + "prod.arundathipatil.me" + "/reset?email="+ email + "&token=" + token);
            messageString.append(",");
            logger.info("============= Pushed message to queue with email : " + email + " and token : " + messageString.toString() + "=======");
            SendMessageRequest messageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl).withMessageBody(messageString.toString());
            amazonSQSClient.sendMessage(messageRequest);
            logger.info("========Message added to queue=====");
            receiveMessageAndDeleteFromQueue();
        } catch (AmazonSQSException exception) {
            if (!exception.getErrorCode().equals("The queue already exists" )) {
                logger.error(exception.getMessage());
                throw exception;
            }
        }
    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void receiveMessageAndDeleteFromQueue() {
        logger.info("Inside receiveMessageAndDelete method");
        String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
        List<Message> receivedMessageList = amazonSQSClient.receiveMessage(amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl()).getMessages();
        for(Message message : receivedMessageList) {
            if (message.getBody() !=null && !message.getBody().isEmpty()) {
                logger.info("Receiving message" + message.getBody());
                amazonSNSClient.publish(message.getBody());
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }
        }
    }
}
