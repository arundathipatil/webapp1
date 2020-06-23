package neu.edu.csye6225.config;

import neu.edu.csye6225.helper.ConstantUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${username}")
    public String userName;
    @Value("${password}")
    public String password;
    @Value("${rdsinstance}")
    public String url;
    @Value("${ACCESS_KEY}")
    public String ACCESS_KEY;
    @Value("${SECRET_KEY}")
    public String SECRET_KEY;
    @Value("${s3bucketname}")
    public String BUCKET_NAME;
    @Bean
    public DataSource getDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUsername("csye6225su2020");
//        dataSource.setPassword("foobarbaz");
//        dataSource.setUrl(
//                "jdbc:mysql://csye6225-su2020.cyhtb2xknabb.us-east-1.rds.amazonaws.com/csye6225?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
//        );

        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setUrl(
                "jdbc:mysql://"+ url + "/csye6225?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
        );
        ConstantUtils.setBucketName(BUCKET_NAME);
        ConstantUtils.setAccessKey(ACCESS_KEY);
        ConstantUtils.setSecretKey(SECRET_KEY);
        return dataSource;
    }
}
