package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.exceptions.PetNotFoundException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.PetForSaleDto;
import ittalents.finalproject.model.pojos.pets.DiscountPet;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController(value = "/pets")
public class PetsInSaleController extends BaseController {

    @Autowired
    PetDao dao;

    @PostMapping("{id}/forSale")
    public Message addForSale(@PathVariable("id") long id, @RequestBody DiscountPet discountPet, HttpSession session)
            throws Exception{
        validateInputForSale(discountPet);
        validateLoginAdmin(session);
        Pet pet = dao.getById(id);
        discountPet.setPetId(id);
        dao.addForSale(pet, discountPet);
        return new Message("Pet successfully added for sale", LocalDateTime.now(), HttpStatus.OK.value());
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


    private void validateInputForSale(DiscountPet pet)throws InvalidInputException {
        if(pet.getDiscountPrice() < 0 || pet.getStartDate() == null || pet.getEndDate() == null
                || pet.getStartDate().compareTo(pet.getEndDate()) > 0
                || pet.getStartDate().compareTo(pet.getEndDate()) == 0){

            throw new InvalidInputException("Invalid input");
        }
    }
}
