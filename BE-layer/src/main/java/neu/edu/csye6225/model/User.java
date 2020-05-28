package neu.edu.csye6225.model;

import javax.persistence.*;

@Entity
//@NamedNativeQueries({
//        @NamedNativeQuery(
//                name    =   "updateEmployeeName",
//                query   =   "UPDATE user SET firstName = ?, lastName = ? WHERE email = ?"
//                ,resultSetMapping = "updateResult"
//        )
//})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
