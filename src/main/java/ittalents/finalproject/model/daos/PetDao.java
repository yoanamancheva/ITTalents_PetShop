package ittalents.finalproject.model.daos;

import ittalents.finalproject.model.DBManager;
import ittalents.finalproject.model.pojos.pets.Pet;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class PetDao {

    @Autowired
    private JdbcTemplate db;

    public void addPet(Pet pet) throws Exception {

        String insertPet = "INSERT INTO pets(gender, breed, sub_breed, age, pet_desc, in_sale, price, quantity) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        db.update(insertPet, (PreparedStatement ps) -> {
                                    ps.setString(1, pet.getGenderPet());
                                    ps.setString(2, pet.getBreed());
                                    ps.setString(3, pet.getSubBreed());
                                    ps.setInt(4, pet.getAge());
                                    ps.setString(5, pet.getDescription());
                                    ps.setBoolean(6, pet.isInSale());
                                    ps.setDouble(7, pet.getPrice());
                                    ps.setInt(8, pet.getQuantity());
        });
    }
}
