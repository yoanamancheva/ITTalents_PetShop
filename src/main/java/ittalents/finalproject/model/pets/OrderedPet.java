package ittalents.finalproject.model.pets;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class OrderedPet {

    private long petId;
    private long userId;

    public OrderedPet(long petId, long userId) {
        this.petId = petId;
        this.userId = userId;
    }
}
