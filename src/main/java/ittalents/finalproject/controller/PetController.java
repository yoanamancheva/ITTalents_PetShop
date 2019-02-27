package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.exceptions.PetNotFoundException;
import ittalents.finalproject.model.daos.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class PetController extends BaseController {

    @Autowired
    PetDao dao;

    //not working
    @PostMapping(value = "/pets/add")
    public void add(HttpServletRequest req, HttpServletResponse resp)throws InvalidInputException {
        try {
            String gender = req.getParameter("genderPet");
            int age = Integer.getInteger(req.getParameter("age")).intValue();
            String breed = req.getParameter("breed");
            String subBreed = req.getParameter("subBreed");
            String description = req.getParameter("description");
            Boolean inSale = Boolean.getBoolean(req.getParameter("inSale"));
            Integer quantity = Integer.getInteger(req.getParameter("quantity"));
            Double price = Double.valueOf(req.getParameter("price"));
            super.validatePetInput(gender, age, breed, subBreed, description, inSale, quantity, price);
            Pet p = new Pet(gender, breed, age, subBreed, description, inSale, price, quantity);
            dao.addPet(p);
            resp.getWriter().append("The pet with id " + p.getId() + " was successfully added");
        } catch (Exception e) {
            System.out.println("OPSI, " + e.getClass().getName() + " " + e.getMessage());
        }
    }

//    @GetMapping(value = "/allProducts")
//    public List<Pet> getAllProducts(){
//
//    }
//
//    @GetMapping(value = "/pets/{id}")
//    public Pet getById(){
//
//    }
//
//    @GetMapping(value = "/pets/{breed}")
//    public Pet getByBreed(){
//
//    }
//
//    @GetMapping(value = "/homepage")
//    public List<Pet> orderedByDate(){
//
//    }
//
//    @PutMapping(value = "/pets/remove/{id}")
//    public void removeById() throws PetNotFoundException {
//
//    }

}
