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

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PetDao {

    public static final String DISC = "disc";
    public static final String ORD = "ord";
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

    public List<PetWithPhotosDto> getAll(){
        String getAll = "SELECT p.id, p.gender, p.breed, p.sub_breed, p.age, p.posted, p.pet_desc, p.price, " +
                    "p.quantity, p.posted FROM pets AS p LEFT JOIN pets_photos AS ph ON p.id = ph.pet_id " +
                    "WHERE ph.id IS NULL ";
        List<PetWithPhotosDto> pets = db.query(getAll, (resultSet, i) ->
                toPetWithPhotos(resultSet, new LinkedList<Photo>()));
        pets.addAll(getPetsWithPhotos());
        return pets;
    }


    public Pet getById(Long id){
        String getById = "SELECT id, gender, breed, sub_breed, age, posted, " +
                "pet_desc, price, quantity FROM pets WHERE id = ?;";
        Pet pet = db.query(getById, new Object[]{id}, (resultSet) ->
                {return returnPet(resultSet, ORD);});
        return pet;
    }

    public void delete(Long id) {
        String deletePet = "DELETE FROM pets WHERE id = ?";
        db.update(deletePet, new Object[] {id});
    }

    public Photo setImage(Photo photo) {
        String setImg = "INSERT INTO pets_photos(photo_path, pet_id) VALUES(?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((con) -> {
            PreparedStatement ps = con.prepareStatement(setImg, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, photo.getPath());
            ps.setLong(2, photo.getPetId());
            return ps;
        }, key);
        photo.setId(key.getKey().longValue());
        return photo;
    }

    public List<Photo> getImagesById(Long id) {
        String getPhotos = "SELECT  ph.id, ph.photo_path, ph.pet_id FROM pets AS p JOIN pets_photos AS ph ON p.id = pet_id" +
                        " WHERE p.id = ?;";
        return db.query(getPhotos, new Object[]{id}, (rs, i) ->
            new Photo(rs.getLong("ph.id"),
                rs.getString("ph.photo_path"),
                rs.getLong("ph.pet_id")));
    }

    public List<PetWithPhotosDto> getPetsWithPhotos() {
        String getPets = "SELECT p.id, ph.id, p.gender, p.breed, p.sub_breed, p.age, p.posted, p.pet_desc, p.quantity, " +
                "ph.photo_path, p.price FROM pets AS p JOIN pets_photos AS ph ON p.id = ph.pet_id";

        List<PetWithPhotosDto> pets = db.query(getPets + " GROUP BY p.id;",
                (rs, i) -> toPetWithPhotos(rs, getImagesById(rs.getLong("p.id"))));
        return pets;
    }


    public PetInSale addForSale(PetInSale petForSale){

        String addForSale = "INSERT INTO pets_in_sale(pet_id, start_date, end_date, discount_price) VALUES(?, ?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        db.update((con) -> {
            PreparedStatement ps = con.prepareStatement(addForSale, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, petForSale.getPetId());
            ps.setTimestamp(2, petForSale.getStartDate());
            ps.setTimestamp(3, petForSale.getEndDate());
            ps.setDouble(4, petForSale.getDiscountPrice());
            return ps;
        }, key);
        petForSale.setId(key.getKey().longValue());

        return petForSale;
    }

    public List<PetForSaleDto> listPetsForSale() {
        String petsForSale = "SELECT p.id, ps.id, ph.id, p.gender, p.breed, p.sub_breed," +
                " p.age, p.posted, p.pet_desc,\n" +
                " ps.discount_price, p.quantity, ps.start_date, ps.end_date, ph.photo_path\n" +
                " FROM pets AS p\n" +
                " JOIN pets_in_sale AS ps\n" +
                " ON p.id = ps.pet_id\n" +
                " LEFT JOIN pets_photos AS ph\n" +
                " ON p.id = ph.pet_id\n" +
                "WHERE ps.start_date <= CURRENT_TIMESTAMP() AND\n" +
                "ps.end_date > CURRENT_TIMESTAMP()";
        List<PetForSaleDto> pets = db.query(petsForSale + " GROUP BY ps.id;", (rs, i) ->
                toPetForSale(rs, getImagesById(rs.getLong("p.id"))));
        return pets;
    }

    public Pet getPetForSaleById(Long id){
        String petForSale = "SELECT p.id, p.gender, p.breed, p.sub_breed, p.age, p.posted, p.pet_desc,\n" +
                            " ps.discount_price, p.quantity " +
                            "FROM pets_in_sale AS ps\n" +
                            "JOIN pets AS p\n" +
                            "ON ps.pet_id = p.id\n" +
                            "WHERE p.id = ? AND " +
                            "ps.start_date <= CURRENT_TIMESTAMP() AND " +
                            "ps.end_date >= CURRENT_TIMESTAMP ();";
        Pet pet = db.query(petForSale, new Object[]{id}, (resultSet) ->
            {return returnPet(resultSet, DISC);});
        System.out.println(pet);
        return pet;
    }


    public Pet getByBreeds(String breed, String subBreed) {
        String getByName = "SELECT id, gender, breed, sub_breed, age, " +
                           "posted, pet_desc, price, quantity, posted " +
                           "FROM pets\n" +
                           "WHERE breed LIKE ? AND sub_breed LIKE ?;";
        Pet pet = db.query(getByName, new Object[]{breed, subBreed},
                (resultSet) -> {return returnPet(resultSet, ORD);});
        return pet;
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

    public int updateOrder(FinalOrderPets order){
        String update = "UPDATE all_orders_pets SET final_price = ? WHERE id = ?";
        int rows = db.update((con) -> {
            PreparedStatement ps = con.prepareStatement(update);
            ps.setDouble(1, order.getFinalPrice());
            ps.setLong(2, order.getId());
            return ps;
        });

        return rows;
    }

    public OrderedPet insertOrderedPet(OrderedPet orderedPet) {
        String insertOrderedPet = "INSERT INTO pets_in_order(order_id, pet_id, quantity) VALUES (?, ?, ?)";
        db.update((con)-> {
            PreparedStatement ps = con.prepareStatement(insertOrderedPet);
            ps.setLong(1, orderedPet.getOrderId());
            ps.setLong(2, orderedPet.getPetId());
            ps.setDouble(3, orderedPet.getQuantity());
            return ps;
        });

        return orderedPet;
    }

    public List<PetWithPhotosDto> filterAndSort(String sortBy, String breed, String subBreed,
                                   Double fromPrice, Double toPrice, String gender, Integer fromAge,
                                   Integer toAge, Timestamp postedAfter) {
        return getAll()
            .stream()
            .filter(pet -> breed == null || pet.getBreed().contains(breed))
            .filter(pet -> subBreed == null || pet.getSubBreed().contains(subBreed))
            .filter(pet -> fromPrice == null || pet.getPrice() >= fromPrice)
            .filter(pet -> toPrice == null || pet.getPrice() < toPrice)
            .filter(pet -> gender == null || pet.getGender().equalsIgnoreCase(gender))
            .filter(pet -> fromAge == null || pet.getAge() >= fromAge)
            .filter(pet -> toAge == null || pet.getAge() < toAge)
            .sorted((pet1, pet2) -> {
                if (sortBy == null) {
                    return 1;
                }
                switch (sortBy) {
                    case "breed":
                        return pet1.getBreed().compareTo(pet2.getBreed());
                    case "subBreed":
                        return pet1.getSubBreed().compareTo(pet2.getSubBreed());
                    case "price":
                        return Double.compare(pet1.getPrice(), pet2.getPrice());
                    case "gender":
                        return pet1.getGender().compareTo(pet2.getGender());
                    case "age":
                        return Integer.compare(pet1.getAge(), pet2.getAge());
                    case "posted":
                        return pet1.getPosted().compareTo(pet2.getPosted());
                    default:
                        return 1;
                }
            })
            .map(pet -> new PetWithPhotosDto(pet.getId(), pet.getGender(), pet.getBreed(),
                    pet.getSubBreed(), pet.getAge(), pet.getPosted(), pet.getPetDesc(),
                    pet.getPrice(), pet.getQuantity(), pet.getPhotos()))
            .collect(Collectors.toList());
    }

    public Pet updateQuantity(Pet pet) {
        String updateQuantity = "UPDATE pets SET quantity = ? WHERE id = ?";
        db.update((con) -> {
            PreparedStatement ps = con.prepareStatement(updateQuantity);
            ps.setInt(1, pet.getQuantity());
            ps.setLong(2, pet.getId());
            return ps;
        });
        return pet;
    }

    private Pet toPetDisc(ResultSet resultSet) throws SQLException{
        Pet pet = new Pet(resultSet.getString("gender"),
                resultSet.getString("breed"),
                resultSet.getString("sub_breed"),
                resultSet.getInt("age"),
                resultSet.getTimestamp("posted"),
                resultSet.getString("pet_desc"),
                resultSet.getDouble("ps.discount_price"),
                resultSet.getInt("quantity"));
        pet.setId(resultSet.getLong("id"));
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

    private Pet returnPet(ResultSet resultSet, String type) throws SQLException{
        boolean hasNext = resultSet.next();
        if(!hasNext) {
            return null;
        }
        else {
            if(type.equals(ORD)) {
                return toPet(resultSet);
            }
            else{
                return toPetDisc(resultSet);
            }
        }
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
                resultSet.getDouble("p.price"),
                resultSet.getInt("p.quantity"),
                photos);
    }

    private Pet toPet(ResultSet resultSet) throws SQLException{
        Pet pet = new Pet(resultSet.getString("gender"),
                resultSet.getString("breed"),
                resultSet.getString("sub_breed"),
                resultSet.getInt("age"),
                resultSet.getTimestamp("posted"),
                resultSet.getString("pet_desc"),
                resultSet.getDouble("price"),
                resultSet.getInt("quantity"));
        pet.setId(resultSet.getLong("id"));
        return pet;
    }

}
