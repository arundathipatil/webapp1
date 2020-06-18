package neu.edu.csye6225.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import neu.edu.csye6225.helper.ConstantUtils;
import neu.edu.csye6225.model.Image;
import neu.edu.csye6225.model.User;
import neu.edu.csye6225.repository.ImageRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.naming.Name;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Service
public class AmazonServiceImpl implements AmazonService{

    private AmazonS3 s3CLinet;

    @Autowired
    private ImageRepository imageRepository;

    @PostConstruct
    private void initializeAmazon() {
        this.s3CLinet = new AmazonS3Client(new BasicAWSCredentials(ConstantUtils.ACCESS_KEY, ConstantUtils.SECRET_KEY));
    }
    @Override
    public String uploadFile(MultipartFile multipartFile, String bookISBN, String userEmail) {
        String fileURl = "";
        JSONObject jsonobj = new JSONObject();
        Image image = new Image();
        try {
            File file = convertMultipartToFile(multipartFile);
            String fileName = new Date().getTime()+"-"+multipartFile.getOriginalFilename().replace(" ", "_");
            fileURl = ConstantUtils.END_POINT_URL+"/"+ConstantUtils.BUCKET_NAME+"/"+fileName;
            s3CLinet.putObject(new PutObjectRequest(ConstantUtils.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            image.setIsbn(bookISBN);
            image.setUserEmail(userEmail);
            image.setImage(fileURl);
            image.setName(fileName);
            image.setDate(new Date());
            imageRepository.save(image);
            jsonobj.put("status", "Success");
            jsonobj.put("imageURL", fileURl);
            jsonobj.put("message", "FIle upload Suceess");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonobj.toString();
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertedfile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertedfile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertedfile;
    }

    @Override
    public  String getFile(String name) {
//        S3Object s3Object = this.s3CLinet.getObject(new GetObjectRequest(ConstantUtils.BUCKET_NAME, name));
//        System.out.println(s3Object.getObjectMetadata());
//        return (MultipartFile) s3Object;

        try{


//            S3Object s3Object = this.s3CLinet.getObject(new GetObjectRequest(ConstantUtils.BUCKET_NAME, name));
//            InputStream is = s3Object.getObjectContent();
//            String extension = s3Object.getObjectMetadata().getContentType();
//            FileInputStream fis = new FileInputStream(String.valueOf(s3Object.getObjectContent()));
//            byte[] bytes = new byte[(int) s3Object.getObjectMetadata().getContentLength()];
//            fis.read(bytes);
//            String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
//            String image = "data:"+extension+";base64,"+encodeBase64;
//
//            return image;


            S3Object s3Object = this.s3CLinet.getObject(new GetObjectRequest(ConstantUtils.BUCKET_NAME, name));
            String extension = s3Object.getObjectMetadata().getContentType();
            InputStream is = s3Object.getObjectContent();
//            Files.copy(is, Paths.get((new Date()).toString() + s3Object.getKey()));
            File f = new File((new Date()).toString() + s3Object.getKey());
            Files.copy(is, Paths.get(f.getPath()));
            FileInputStream fis = new FileInputStream(f);
//            FileInputStream fis = new FileInputStream(new File((new Date()).toString() + s3Object.getKey()););
            byte[] bytes = new byte[(int) s3Object.getObjectMetadata().getContentLength()];
            fis.read(bytes);
            String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
            String image = "data:"+extension+";base64,"+encodeBase64;
            return image;


//            S3Object s3Object = this.s3CLinet.getObject(new GetObjectRequest(ConstantUtils.BUCKET_NAME, name));
//            InputStream is = s3Object.getObjectContent();
//            String extension = s3Object.getObjectMetadata().getContentType();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int length;
//            byte[] buffer = new byte[4096];
//            while ((length = is.read(buffer, 0, buffer.length)) != -1) {
//                baos.write(buffer, 0, length);
//            }
//
//            // writing to file
//            S3ObjectInputStream s3is = s3Object.getObjectContent();
//            File file = new File(name);
//            FileOutputStream fos = new FileOutputStream(file);
//            byte[] read_buf = new byte[1024];
//            int read_len = 0;
//            while ((read_len = s3is.read(read_buf)) > 0) {
//                fos.write(read_buf, 0, read_len);
//            }
//
//
////            FileInputStream fis = new FileInputStream(file);
////            byte[] bytes = new byte[(int) file.length()];
////            fis.read(bytes);
//            String encodeBase64 = Base64.getEncoder().encodeToString(read_buf);
//            String img = "data:"+extension+";base64,"+encodeBase64;
//            fos.close();
//            return img;

//            return baos;
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (AmazonS3Exception err) {
            System.out.println(err.getMessage());
        } catch (AmazonClientException err) {
            System.out.println(err.getMessage());
        }
        return null;
    }

    @Override
     public String deleteFile(String name) {
        try{
            s3CLinet.deleteObject(ConstantUtils.BUCKET_NAME, name);
            return "SUCCESS";
        } catch (AmazonS3Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
     }
}
