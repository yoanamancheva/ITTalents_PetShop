package ittalents.finalproject.controller;

import ittalents.finalproject.model.daos.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PetController {

    @Autowired
    PetDao dao;

    @PostMapping(value = "/pets/add")
    public void add(HttpServletRequest req, HttpServletResponse resp){
        try {
            String gender = req.getParameter("genderPet");
            int age = Integer.getInteger(req.getParameter("age")).intValue();
            String breed = req.getParameter("breed");
            String subBreed = req.getParameter("subBreed");
            String description = req.getParameter("description");
            Boolean inSale = Boolean.getBoolean(req.getParameter("inSale"));
            Integer quantity = Integer.getInteger(req.getParameter("quantity"));
            Double price = Double.valueOf(req.getParameter("price"));
            if(validatePet(gender, age, breed, subBreed, description, inSale, quantity, price)){
                Pet p = new Pet(gender, breed, age, subBreed, description, inSale, price, quantity);
                dao.addPet(p);
                resp.getWriter().append("The pet with id " + p.getId() + " was successfully added");
            }
            else{
                resp.setStatus(400);
                resp.getWriter().append("There are some problems adding a pet, Try again later");
            }
        } catch (Exception e) {
            System.out.println("OPSI, " + e.getClass().getName() + " " + e.getMessage());
        }
    }

    public static boolean validatePet(String gender, int age, String breed, String subBreed, String description, Boolean inSale, int quantity, double price){
        //TODO more validations for user
        if(gender == null || age < 0 || breed == null || subBreed == null || description == null || quantity < 1 || price < 0){
            return false;
        }
        else {
            return true;
        }
    }
}
