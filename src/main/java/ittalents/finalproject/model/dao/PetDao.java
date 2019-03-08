package ittalents.finalproject.model.dao;

import ittalents.finalproject.model.pojos.dto.PetForSaleDto;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.orders.FinalOrderPets;
import ittalents.finalproject.model.pojos.pets.OrderedPet;
import ittalents.finalproject.model.pojos.pets.PetInSale;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PetDao {

    @Autowired
    private JdbcTemplate db;

    public Pet addPet(Pet pet){

        String insertPet = "INSERT INTO pets(gender, breed, sub_breed, age, pet_desc, price, quantity) VALUES(?, ?, ?, ?, ?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((con)-> {
            PreparedStatement ps = con.prepareStatement(insertPet, Statement.RETURN_GENERATED_KEYS);
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
        return pet;
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
        String getById = "SELECT id, gender, breed, sub_breed, age, posted, " +
                "pet_desc, in_sale, price, quantity FROM pets WHERE id = ?;";
        Pet pet = db.query(getById, new Object[]{id}, (resultSet) ->
                {return returnPet(resultSet);});
        return pet;
    }

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
                "ph.photo_path, p.price FROM pets AS p JOIN pets_photos AS ph ON p.id = ph.pet_id";
        List<Photo> photos = db.query(getPets, (rs, i) -> toPhoto(rs));

        List<PetWithPhotosDto> pets = db.query(getPets + "  GROUP BY p.id;", (rs, i) -> toPetWithPhotos(rs, photos));
        return pets;
    }

    private PetWithPhotosDto toPetWithPhotos(ResultSet resultSet, List<Photo> photos) throws SQLException{
        return new PetWithPhotosDto(
            resultSet.getLong("p.id"),
            resultSet.getString("p.gender"),
            resultSet.getString("p.breed"),
            resultSet.getString("p.sub_breed"),
            resultSet.getInt("p.age"),
            resultSet.getTimestamp("p.posted"),
            resultSet.getString("p.pet_desc"),
            resultSet.getBoolean("p.in_sale"),
            resultSet.getDouble("p.price"),
            resultSet.getInt("p.quantity"),
            photos);
    }


    public void addForSale(Pet pet, PetInSale petForSale)throws Exception{
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.getDataSource().getConnection();
            con.setAutoCommit(false);
            String addForSale = "INSERT INTO pets_in_sale(pet_id, start_date, end_date, discount_price) VALUES(?, ?, ?, ?);";
            String updatePet = "UPDATE pets SET in_sale = 1 WHERE id = ?";

            ps = con.prepareStatement(addForSale, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, petForSale.getPetId());
            ps.setTimestamp(2, petForSale.getStartDate());
            ps.setTimestamp(3, petForSale.getEndDate());
            ps.setDouble(4, petForSale.getDiscountPrice());
            ps.executeUpdate();
            petForSale.setId(ps.getGeneratedKeys().getLong("id"));

            ps = con.prepareStatement(updatePet);
            ps.setLong(1, pet.getId());
            ps.executeUpdate();

            con.commit();
            System.out.println("Transaction made successfully");
        }
        catch(Exception e){
            con.rollback();
            throw new Exception();
        }
        finally {
            con.setAutoCommit(true);
            con.close();
            ps.close();
        }
    }

    public List<PetForSaleDto> listPetsForSale() {
        String petsForSale = "SELECT p.id, ps.id, ph.id, p.gender, p.breed, p.sub_breed," +
                " p.age, p.posted, p.pet_desc,\n" +
                " ps.discount_price, p.quantity, ps.start_date, ps.end_date, ph.photo_path\n" +
                " FROM pets AS p\n" +
                " JOIN pets_in_sale AS ps\n" +
                " ON p.id = ps.pet_id\n" +
                " JOIN pets_photos AS ph\n" +
                " ON p.id = ph.pet_id\n" +
                "WHERE ps.start_date <= CURRENT_DATE() AND\n" +
                "ps.end_date > CURRENT_DATE();";
        List<Photo> photos = db.query(petsForSale, (rs, i) -> toPhoto(rs));
        List<PetForSaleDto> pets = db.query(petsForSale + " GROUP BY ps.id;", (rs, i) -> toPetForSale(rs, photos));
        return pets;
    }

    public Pet getPetForSaleById(long id){
        String petForSale = "SELECT p.id, p.gender, p.breed, p.sub_breed, p.age, p.posted, p.pet_desc,\n" +
                            " ps.discount_price AS price, p.quantity, p.in_sale" +
                            "FROM pets_in_sale AS ps\n" +
                            "JOIN pets AS p\n" +
                            "ON ps.pet_id = p.id\n" +
                            "WHERE p.id = ? AND" +
                            "ps.start_date <= CURRENT_DATE() AND" +
                            "ps.end_date >= CURRENT_DATE();";
        Pet pet = db.query(petForSale, new Object[]{id}, (resultSet) ->
            {return returnPet(resultSet);});
        return pet;
    }

    private Photo toPhoto(ResultSet rs) throws SQLException{
        return new Photo(rs.getLong("ph.id"),
                rs.getString("photo_path"),
                rs.getLong("p.id"));
    }

    private PetForSaleDto toPetForSale(ResultSet rs, List<Photo> photos) throws SQLException{
        return new PetForSaleDto(
                rs.getLong("p.id"),
                rs.getString("gender"),
                rs.getString("breed"),
                rs.getString("p.sub_breed"),
                rs.getInt("age"),
                rs.getString("pet_desc"),
                rs.getInt("quantity"),
                rs.getLong("ps.id"),
                rs.getTimestamp("start_date"),
                rs.getTimestamp("end_date"),
                rs.getDouble("ps.discount_price"),
                photos);
    }

    public Pet getByBreeds(String breed, String subBreed) {
        String getByName = "SELECT id, gender, breed, sub_breed, age, " +
                           "posted, pet_desc, in_sale, price, quantity, posted " +
                           "FROM pets\n" +
                           "WHERE breed LIKE ? AND sub_breed LIKE ?;";
        Pet pet = db.query(getByName, new Object[]{breed, subBreed},
                (resultSet) -> {return returnPet(resultSet);});
        return pet;
    }

    public Pet returnPet(ResultSet resultSet) throws SQLException{
        boolean hasNext = resultSet.next();
        if(!hasNext) {
            return null;
        }
        else {
            return toPet(resultSet);
        }
    }

    public FinalOrderPets insertOrder(FinalOrderPets order) {
        String insertOrder = "INSERT INTO all_orders_pets(address, user_id, final_price) VALUES (?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((con)-> {
            PreparedStatement ps = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getAddress());
            ps.setLong(2, order.getUserId());
            ps.setDouble(3, order.getFinalPrice());
            return ps;
        }, key);
        order.setId(key.getKey().longValue());
        order.setDate(Timestamp.valueOf(LocalDateTime.now()));

        return order;
    }

    public OrderedPet insertOrderedPet(OrderedPet orderedPet) {
        String insertOrderedPet = "INERT INTO pets_in_order(order_id, pet_id, quantity) VALUES (?, ?, ?)";
        db.update((con)-> {
            PreparedStatement ps = con.prepareStatement(insertOrderedPet);
            ps.setLong(1, orderedPet.getOrderId());
            ps.setLong(2, orderedPet.getPetId());
            ps.setDouble(3, orderedPet.getQuantity());
            return ps;
        });

        return orderedPet;
    }
}
