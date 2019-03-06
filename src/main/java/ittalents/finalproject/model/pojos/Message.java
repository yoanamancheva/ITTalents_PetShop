package ittalents.finalproject.model.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    private String message;
    private LocalDateTime time;
    private int status;
}
