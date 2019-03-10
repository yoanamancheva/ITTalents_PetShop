package ittalents.finalproject.controller;

import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.PetForSaleDto;
import ittalents.finalproject.model.pojos.pets.PetInSale;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController()
@RequestMapping(value = "/pets")
public class PetsInSaleController extends BaseController {

    @Autowired
    PetDao dao;

    @PostMapping("{id}/forSale")
    public Object addForSale(@PathVariable("id") long id, @RequestBody PetInSale petInSale, HttpSession session)
            throws Exception {
        validateInputForSale(petInSale);
        validateLoginAdmin(session);
        petInSale.setPetId(id);
        petInSale = dao.addForSale(petInSale);
        if(petInSale != null){
            return petInSale;
        }
        else{
            return new Message("Pet not successfully added for sale", LocalDateTime.now(), HttpStatus.OK.value());
        }
    }

    @GetMapping("/forSale")
    public List<PetForSaleDto> listPetsInSale()throws PetNotFoundException{
        List<PetForSaleDto> petsForSale = dao.listPetsForSale();
        if(petsForSale != null){
            return petsForSale;
        }
        else{
            throw new PetNotFoundException();
        }
    }

    @GetMapping(value = "/{id}/forSale")
    public Pet getPetForSaleById(@PathVariable long id, HttpSession session) throws PetNotFoundException{
        Pet pet = dao.getPetForSaleById(id);
        if(pet != null){
            return pet;
        }
        else{
            throw new PetNotFoundException();
        }
    }


    private void validateInputForSale(PetInSale pet)throws InvalidInputException {
        if(pet.getDiscountPrice() < 0 || pet.getStartDate() == null || pet.getEndDate() == null
                || pet.getStartDate().compareTo(pet.getEndDate()) > 0
                || pet.getStartDate().compareTo(pet.getEndDate()) == 0){

            throw new InvalidInputException("Invalid input");
        }
    }
}
