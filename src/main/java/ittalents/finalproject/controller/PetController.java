package ittalents.finalproject.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ittalents.finalproject.model.daos.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Scanner;

@RestController
public class PetController {

    @Autowired
    PetDao dao;

    @PostMapping(value = "/pets/add")
    public void add(HttpServletRequest req, HttpServletResponse resp){
        try {
            StringBuilder text = new StringBuilder();
            Scanner sc = new Scanner(req.getReader());
            while(sc.hasNextLine()){
                text.append(sc.nextLine());
            }
            String request = text.toString();
            JsonObject json = new JsonParser().parse(request).getAsJsonObject();
            Pet p = new Pet(json.get("genderPet").toString(),
                    json.get("breed").toString(),
                    json.get("age").getAsInt(),
                    json.get("subBreed").getAsString(),
                    json.get("description").getAsString(),
                    json.get("inSale").getAsBoolean(),
                    json.get("age").getAsDouble(),
                    json.get("quantity").getAsInt());
            if(validatePet(p)){
                dao.addPet(p);
                resp.getWriter().append("The pet with id " + p.getId() + " was successfuly added");
            }
            else{
                resp.setStatus(400);
                resp.getWriter().append("There are some problems adding a pet, Try again later");
            }
        } catch (Exception e) {
            System.out.println("OPSI, " + e.getClass().getName());
        }
    }

    public static boolean validatePet(Pet pet){
        //TODO more validations
        if(pet.getGenderPet() == null || pet.getAge() == 0 || pet.getBreed() == null || pet.getDescription() == null || pet.getPrice() == 0 || pet.getSubBreed() == null || pet.getQuantity() == 0){
            return false;
        }
        else
            return true;
    }
}
