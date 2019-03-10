package ittalents.finalproject.service;

import ittalents.finalproject.controller.PetController;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.dto.PetForSaleDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.PetInSale;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetInSaleService {

    @Autowired
    PetDao dao;

    @Autowired
    PetController petCtrl;

    public Object addForSale(Long id, PetInSale petInSale)
            throws BaseException {
        petCtrl.validatePetId(id);
        petInSale.setPetId(id);
        petInSale = dao.addForSale(petInSale);
        if(petInSale != null){
            return petInSale;
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public List<PetForSaleDto> listPetsInSale()throws PetNotFoundException{
        List<PetForSaleDto> petsForSale = dao.listPetsForSale();
        if(petsForSale != null){
            return petsForSale;
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public Pet getPetForSaleById(Long id) throws PetNotFoundException{
        Pet pet = null;
        petCtrl.validatePetId(id);
        pet = dao.getPetForSaleById(id);
        if(pet != null){
            return pet;
        }
        else{
            throw new PetNotFoundException();
        }
    }
}
