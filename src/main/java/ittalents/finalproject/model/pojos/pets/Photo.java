package ittalents.finalproject.model.pojos.pets;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Photo {
    private long id;
    private String path;
    private long petId;

    public Photo(String path, long petId) {
        this.path = path;
        this.petId = petId;
    }
}
