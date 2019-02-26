package ittalents.finalproject.model.daos;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ittalents.finalproject.model.DBManager;
import ittalents.finalproject.model.pets.Pet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

@Component
public class PetDao {

//    @Autowired
//    private JdbcTemplate db;

    public void addPet(Pet pet) throws Exception {

        String insertPet = "INSERT INTO pets(gender, breed, sub_breed, age, pet_desc, in_sale, price, quantity) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(insertPet, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, pet.getGenderPet());
        ps.setString(2, pet.getBreed());
        ps.setString(3, pet.getSubBreed());
        ps.setInt(4, pet.getAge());
        ps.setString(5, pet.getDescription());
        ps.setBoolean(6, pet.isInSale());
        ps.setDouble(7, pet.getPrice());
        ps.setInt(8, pet.getQuantity());
        ps.executeUpdate();
    }
}
