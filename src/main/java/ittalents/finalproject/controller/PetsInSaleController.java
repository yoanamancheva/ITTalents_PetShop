package ittalents.finalproject.controller;

import ittalents.finalproject.service.PetInSaleService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import ittalents.finalproject.model.pojos.dto.PetForSaleDto;
import ittalents.finalproject.model.pojos.pets.PetInSale;
import ittalents.finalproject.model.pojos.pets.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController()
@RequestMapping(value = "/pets")
public class PetsInSaleController extends BaseController {

    @Autowired
    PetInSaleService service;

    @PostMapping("{id}/forSale")
    public Object addForSale(@PathVariable("id") Long id, @RequestBody PetInSale petInSale, HttpSession session)
             throws BaseException{
        petInSale.setId(id);
        validateInputForSale(petInSale);
        validateLoginAdmin(session);
        return service.addForSale(id, petInSale);
    }

    @GetMapping("/forSale")
    public List<PetForSaleDto> listPetsInSale()throws PetNotFoundException{
        return service.listPetsInSale();
    }

    @GetMapping(value = "/{id}/forSale")
    public Pet getPetForSaleById(@PathVariable Long id) throws PetNotFoundException{
       return service.getPetForSaleById(id);
    }


    private void validateInputForSale(PetInSale pet)throws InvalidInputException {
        if(pet.getStartDate() == null || pet.getEndDate() == null ||
                pet.getDiscountPrice() == null || pet.getDiscountPrice() < 0 ||
                pet.getStartDate().compareTo(pet.getEndDate()) > 0
                || pet.getStartDate().compareTo(pet.getEndDate()) == 0){

            throw new InvalidInputException("Invalid input");
        }
    }
}
