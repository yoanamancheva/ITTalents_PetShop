package ittalents.finalproject.model.daos;

import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PetDao {

    @Autowired
    private JdbcTemplate db;

    public void addPet(Pet pet){

        String insertPet = "INSERT INTO pets(gender, breed, sub_breed, age, pet_desc, in_sale, price, quantity) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((connection)-> {
            PreparedStatement ps = connection.prepareStatement(insertPet, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pet.getGender());
            ps.setString(2, pet.getBreed());
            ps.setString(3, pet.getSubBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getPetDesc());
            ps.setBoolean(6, pet.isInSale());
            ps.setDouble(7, pet.getPrice());
            ps.setInt(8, pet.getQuantity());
            return ps;
            }, key);
        pet.setPosted(LocalDateTime.now());
        pet.setId(key.getKey().longValue());
    }

    public List<Pet> getAll(){
        String getAll = "SELECT gender, breed, sub_breed, age, pet_desc, in_sale, price, quantity, posted FROM pets";
        List<Pet> pets = db.query(getAll, (resultSet, i) -> toPet(resultSet));
        return pets;
    }

    private Pet toPet(ResultSet resultSet) throws SQLException{
        Pet pet = new Pet(resultSet.getString("gender"),
                        resultSet.getString("breed"),
                        resultSet.getString("sub_breed"),
                        resultSet.getInt("age"),
                        resultSet.getString("pet_desc"),
                        resultSet.getBoolean("in_sale"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"));
        return pet;
    }


}
