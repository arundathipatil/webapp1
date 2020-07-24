package neu.edu.csye6225.aws;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;



import com.amazonaws.services.sqs.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

public class AmazonSQSClient {

    private AmazonSQS amazonSQSClient;
    private final static String QUEUE = "password-reset-queue";
    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private Logger logger = LoggerFactory.getLogger(AmazonSQSClient.class);


    @PostConstruct
    private void init() {
        this.amazonSQSClient = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
    }


    public void sendMessageToQueue(String email) {
        try {
            receiveMessageAndDeleteFromQueue();
            CreateQueueResult create_result = amazonSQSClient.createQueue(QUEUE);
            String queueUrl = amazonSQSClient.getQueueUrl(QUEUE).getQueueUrl();
            StringBuilder messageString = new StringBuilder();
            messageString.append(email);

            SendMessageRequest messageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl).withMessageBody(messageString.toString());
            amazonSQSClient.sendMessage(messageRequest);
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
                amazonSNSClient.publish("password-reset", message.getBody());
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }
        }
    }
}
