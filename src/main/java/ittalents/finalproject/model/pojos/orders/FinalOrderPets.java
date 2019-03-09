package ittalents.finalproject.model.pojos.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FinalOrderPets {
    private long id;
    private Timestamp date;
    private String address;
    private long userId;
    private double finalPrice;

    public FinalOrderPets(String address){
        this.address = address;
    }
}
