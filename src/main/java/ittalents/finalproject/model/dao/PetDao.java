package ittalents.finalproject.model.dao;

import com.mysql.cj.util.StringUtils;
import ittalents.finalproject.exceptions.PetNotFoundException;
import ittalents.finalproject.model.pojos.dto.ImageUploadDto;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class PetDao {

    @Autowired
    private JdbcTemplate db;

    public void addPet(Pet pet){

        String insertPet = "INSERT INTO pets(gender, breed, sub_breed, age, pet_desc, price, quantity) VALUES(?, ?, ?, ?, ?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((connection)-> {
            PreparedStatement ps = connection.prepareStatement(insertPet, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pet.getGender());
            ps.setString(2, pet.getBreed());
            ps.setString(3, pet.getSubBreed());
            ps.setInt(4, pet.getAge());
            ps.setString(5, pet.getPetDesc());
            ps.setDouble(6, pet.getPrice());
            ps.setInt(7, pet.getQuantity());
            return ps;
            }, key);
        pet.setPosted(Timestamp.valueOf(LocalDateTime.now()));
        pet.setId(key.getKey().longValue());
    }

    public List<Pet> getAll(){
        String getAll = "SELECT id, gender, breed, sub_breed, age, posted, pet_desc, in_sale, price, quantity, posted FROM pets ORDER BY posted DESC;";
        List<Pet> pets = db.query(getAll, (resultSet, i) -> toPet(resultSet));
        return pets;
    }

    private Pet toPet(ResultSet resultSet) throws SQLException{
        Pet pet = new Pet(resultSet.getString("gender"),
                resultSet.getString("breed"),
                resultSet.getString("sub_breed"),
                resultSet.getInt("age"),
                resultSet.getTimestamp("posted"),
                resultSet.getString("pet_desc"),
                resultSet.getBoolean("in_sale"),
                resultSet.getDouble("price"),
                resultSet.getInt("quantity"));
        pet.setId(resultSet.getLong("id"));
        return pet;
    }

    public Pet getById(long id){
        String getById = "SELECT id, gender, breed, sub_breed, age, posted, pet_desc, in_sale, price, quantity FROM pets WHERE id = ?;";
        Pet pet = db.query(getById, new Object[]{id}, (resultSet) -> {
            resultSet.next();
            return toPet(resultSet);
        });
        return pet;
    }

    //into dto to add photos
//    public List<Pet> getFiltred(String breed, String subBreed, int fromAge, int toAge, String gender,
//                                double fromPrice, double toPrice, String sortBy, String ascDesc) {
//
//        String selectClause = "SELECT * FROM EMPLOYEES WHERE ";
//        if(StringUtils.isNotBlank(empName)){
//            selectQuery += "AND EMP_NAME = " + empName;
//        }
//        if(StringUtils.isNotBlank(empID)){
//            selectQuery += "AND EMP_ID = " + empID;
//
//        String filter;
//        List<Pet> pets = null;
//        if(subBreed != null && fromAge > -1 && toAge > -1 && gender != null && fromPrice > 0 && toPrice > 0 &&
//            sortBy != null && ascDesc != null) {
//            if (fromAge > toAge) {
//                int temp = fromAge;
//                fromAge = toAge;
//                toAge = temp;
//            } else if (fromPrice > toPrice) {
//                double temp = fromPrice;
//                fromPrice = toPrice;
//                toPrice = temp;
//            }
//            filter = "SELECT id, gender, breed, sub_breed, age, posted, pet_desc, in_sale, price, quantity FROM pets WHERE (age >= ? AND age <= ?)" +
//                    " AND breed LIKE ? AND sub_breed LIKE ? AND gender LIKE ? && (price >= ? AND price <= ?) ORDER BY ? ?" +
//                    " ORDER BY ? ?";
//            pets = db.query(filter, new Object[]{fromAge, toAge, breed, subBreed, gender, fromPrice, toPrice}, (resultSet, i) -> toPet(resultSet));
//        }
////       else if(){
////
//       }
//
//
//
//       return pets;
//    }

    public void delete(long id) {
        String deletePet = "DELETE FROM pets WHERE id = ?";
        db.update(deletePet, new Object[] {id});
    }


    public void setImage(Photo photo) {
        String setImg = "INSERT INTO pets_photos(photo_path, pet_id) VALUES(?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((con) -> {
            PreparedStatement ps = con.prepareStatement(setImg, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, photo.getPath());
            ps.setLong(2, photo.getPetId());
            return ps;
        }, key);
        photo.setId(key.getKey().longValue());
    }

    public List<Photo> getImagesById(long id) {
        String getPhotos = "SELECT  ph.id, ph.photo_path, ph.pet_id FROM pets AS p JOIN pets_photos AS ph ON ? = pet_id;";
        return db.query(getPhotos, (rs, i) -> {return new Photo(rs.getLong("ph.id"),
                                         rs.getString("ph.photo_path"),
                                         rs.getLong("ph.pet_id"));},
                            new Object[]{id});
    }

    public List<PetWithPhotosDto> getPetsWithPhotos() {
        String getPets = "SELECT p.id, ph.id, p.gender, p.breed, p.sub_breed, p.age, p.posted, p.pet_desc, p.in_sale, p.quantity, " +
                "ph.photo_path, p.price FROM pets AS p JOIN pets_photos AS ph ON p.id = ph.pet_id;";
        List<Photo> photos = db.query(getPets, (rs, i) -> {return new Photo(rs.getLong("ph.id"),
                rs.getString("ph.photo_path"),
                rs.getLong("p.id"));});

        List<PetWithPhotosDto> pets = db.query(getPets, (rs, i) -> {return new PetWithPhotosDto(
                rs.getLong("p.id"),
                rs.getString("p.gender"),
                rs.getString("p.breed"),
                rs.getString("p.sub_breed"),
                rs.getInt("p.age"),
                rs.getTimestamp("p.posted"),
                rs.getString("p.pet_desc"),
                rs.getBoolean("p.in_sale"),
                rs.getDouble("p.price"),
                rs.getInt("p.quantity"),
                photos);});
        return pets;
    }

    public void addForSale(Pet pet) {
        String addForSale = "INSERT INTO pets_in_sale(pet_id, start_date, end_date, discount_price) VALUES(?, ?, ?, ?);";

    }
}
