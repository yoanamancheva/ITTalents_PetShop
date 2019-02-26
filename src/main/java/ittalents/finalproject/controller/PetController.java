package ittalents.finalproject.controller;

<<<<<<< HEAD
=======
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
>>>>>>> a429fafc6a69c4b070fbaa586368ec0db3e2ed6e
import ittalents.finalproject.model.daos.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
=======
import java.util.Scanner;
>>>>>>> a429fafc6a69c4b070fbaa586368ec0db3e2ed6e

@RestController
public class PetController {

    @Autowired
    PetDao dao;

    @PostMapping(value = "/pets/add")
    public void add(HttpServletRequest req, HttpServletResponse resp){
        try {
//            StringBuilder text = new StringBuilder();
//            Scanner sc = new Scanner(req.getReader());
//            while(sc.hasNextLine()){
//                text.append(sc.nextLine());
//            }
//            String request = text.toString();
//            JsonObject json = new JsonParser().parse(request).getAsJsonObject();
            String gender = req.getParameter("genderPet");
            int age = Integer.getInteger(req.getParameter("age")).intValue();
            String breed = req.getParameter("breed");
            String subBreed = req.getParameter("subBreed");
            String description = req.getParameter("description");
            Boolean inSale = Boolean.getBoolean(req.getParameter("inSale"));
            Integer quantity = Integer.getInteger(req.getParameter("quantity"));
            Double price = Double.valueOf(req.getParameter("price"));
            Pet p = new Pet(gender, breed, age, subBreed, description, inSale, price, quantity);
            if(validatePet(p)){
                dao.addPet(p);
                resp.getWriter().append("The pet with id " + p.getId() + " was successfully added");
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
        //TODO more validations for user
        if(pet.getGenderPet() == null || pet.getAge() == 0 || pet.getBreed() == null || pet.getDescription() == null || pet.getPrice() == 0 || pet.getSubBreed() == null || pet.getQuantity() == 0){
            return false;
        }
        else
            return true;
    }
}
