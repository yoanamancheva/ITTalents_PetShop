package ittalents.finalproject.model.pojos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @Transient
    private String password2;
    private String firstName;
    private String lastName;
    private String email;
    private boolean administrator;
    private boolean notifications;
    private boolean verified;


//    @JoinColumn(name="id",referencedColumnName="user_id")
//    private Set<Review> reviews;


    public User(String username, String password, String firstName, String lastName, String email, boolean notifications) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.administrator = false;
        this.notifications = notifications;
        this.verified = false;
    }

}
