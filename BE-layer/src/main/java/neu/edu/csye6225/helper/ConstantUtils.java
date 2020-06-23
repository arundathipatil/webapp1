package neu.edu.csye6225.helper;

import org.springframework.beans.factory.annotation.Value;

public class ConstantUtils {

    public String s3Bucket;
    public static final String END_POINT_URL = "https://s3.us-east-1.amazonaws.com";
//    @Value("${ACCESS_KEY}")
    public static String ACCESS_KEY;
//    @Value("${SECRET_KEY}")
    public static String SECRET_KEY;
//    @Value("${s3bucketname}")
    public static String BUCKET_NAME;

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public static String getAccessKey() {
        return ACCESS_KEY;
    }

    public static void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static String getBucketName() {
        return BUCKET_NAME;
    }

    public static void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }
}

