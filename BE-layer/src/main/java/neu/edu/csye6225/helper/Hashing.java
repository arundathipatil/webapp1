package neu.edu.csye6225.helper;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.codec.binary.Hex;

public class Hashing {
    private static final String salt = "1234";
    private  static final int iterations = 10000;
    private static final int keyLength = 512;

    public static String hashPassword(String pwd) {
        try {
            char[] passwordChars = pwd.toCharArray();
            byte[] saltBytes = Hashing.salt.getBytes();
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( passwordChars, saltBytes,  iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            String hashedString = Hex.encodeHexString(res);
            return hashedString;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
