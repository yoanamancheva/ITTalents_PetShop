package ittalents.finalproject.model.pojos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class ImageUploadDto {

    private String title;
    private String imageStr;

    public ImageUploadDto(String imageStr){
        this.imageStr = imageStr;
    }

}
