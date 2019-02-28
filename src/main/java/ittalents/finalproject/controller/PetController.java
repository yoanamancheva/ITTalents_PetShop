package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.model.daos.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PetController extends BaseController {

    @Autowired
    private PetDao dao;

    @PostMapping(value = "/pets/add")
    public @ResponseBody String add(@RequestBody Pet pet, HttpSession session) throws BaseException {

        if(pet.getGender() == null || pet.getAge() < 0 || pet.getBreed() == null || pet.getSubBreed() == null ||
                pet.getPetDesc() == null || pet.getQuantity() < 1 || pet.getPrice() < 0){
            throw new InvalidInputException();
        }
        super.validateLoginAdmin(session);
        dao.addPet(pet);
        System.out.println(pet.toString());
        return "The pet with id " + pet.getId() + " was successfully added";
    }

    @GetMapping(value = "/pets")
    public @ResponseBody List<Pet> getAll(){
        return dao.getAll();
    }

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
