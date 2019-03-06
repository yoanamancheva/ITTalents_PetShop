package ittalents.finalproject.model.pojos.dto;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ToString
public class ChangePasswordUserDTO {
    private String username;
    private String password;
    private String newPassword;
}
