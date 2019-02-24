package ittalents.finalproject.model.pets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class DiscountPet {

    private long id;
    private long petId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int discountPrice;

    public DiscountPet(long petId, LocalDateTime startDate, LocalDateTime endDate, int discountPrice) {
        this.petId = petId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPrice = discountPrice;
    }
}
