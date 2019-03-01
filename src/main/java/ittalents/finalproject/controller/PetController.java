package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/pets")
public class PetController extends BaseController {

    @Autowired
    private PetDao dao;

    @PostMapping(value = "/add")
    public @ResponseBody String add(@RequestBody Pet pet, HttpSession session) throws BaseException {

        if(pet.getGender() == null || pet.getAge() < 0 || pet.getBreed() == null || pet.getSubBreed() == null ||
                pet.getPetDesc() == null || pet.getQuantity() < 1 || pet.getPrice() < 0){
            throw new InvalidInputException("Invalid input.");
        }
        super.validateLoginAdmin(session);
        dao.addPet(pet);
        System.out.println(pet.toString());
        return "The pet with id " + pet.getId() + " was successfully added";
    }

    @GetMapping()
    public @ResponseBody List<Pet> getAll(){
        return dao.getAll();
    }

    @GetMapping(value = "/{id}")
    public Pet getById(@PathVariable long id){
        return dao.getById(id);
    }

    @GetMapping(value = "/{breed}")
    public List<Pet> getByBreed(@RequestParam String breed){
        return dao.getFiltred(breed, null, -1, -1, null, -1,
                -1, null, null);
    }

//    @GetMapping(value = "/{breed}/{subBreed}")
//    public List<Pet> getByBreedSubBreed(@PathVariable String breed, @PathVariable String subBreed){
//        return dao.getFiltred(breed, subBreed);
//    }


    @PutMapping(value = "/remove/{id}")
    public @ResponseBody String removeById(@PathVariable long id, HttpSession session) throws BaseException {
        //validateLoginAdmin(session);
        dao.delete(id);
        return "Successfully deleted pet!";
    }
}
