package ittalents.finalproject.service;

import ittalents.finalproject.controller.ImageController;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.ImageCannotBeAddedException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetDao dao;

    @Autowired
    private ImageController imgCtrl;

    public Pet add(Pet pet) throws BaseException {
        validateInput(pet);
        return dao.addPet(pet);
    }


    public List<PetWithPhotosDto> filterAndSort(String sortBy,
                                                String breed,
                                                String subBreed,
                                                Double fromPrice,
                                                Double toPrice,
                                                String gender,
                                                Integer fromAge,
                                                Integer toAge,
                                                Timestamp postedAfter) {
        if(sortBy == null && breed == null && subBreed == null && fromPrice == null &&
                toPrice == null && gender == null && fromAge == null && toAge == null && postedAfter == null) {
            return dao.getAll();
        }
        else {
            return dao.filterAndSort(sortBy, breed, subBreed,
                    fromPrice, toPrice, gender, fromAge, toAge, postedAfter);
        }
    }


    public Object getAll() {
        List<PetWithPhotosDto> pets = dao.getAll();
        if(pets.isEmpty()){
            return new Message("There are no pets for sale", LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
        }
        else{
            return pets;
        }
    }


    public Pet getById(Long id) throws PetNotFoundException {
        Pet pet = null;
        if(id != null){
            pet = dao.getById(id);
        }
        if(pet != null){
            return pet;
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public Message removeById( Long id) throws BaseException {
        if(id != null && getById(id) != null) {
            dao.delete(id);
            return new Message("Successfully deleted pet!", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public Photo setPetImage(Long id, MultipartFile img, HttpSession ses) throws BaseException, IOException {
        if(!img.isEmpty() && id != null && getById(id) != null) {
            String imgTitle = imgCtrl.uploadImage(img, ses);
            Photo photo = new Photo(imgTitle, id);
            return dao.setImage(photo);
        }
        else{
            throw new ImageCannotBeAddedException();
        }
    }


    public PetWithPhotosDto getPetImages(Long id)throws PetNotFoundException{
        if(id != null) {
            Pet p = this.getById(id);
            List<Photo> photos = dao.getImagesById(id);
            return new PetWithPhotosDto(p.getId(), p.getGender(),
                    p.getBreed(), p.getSubBreed(), p.getAge(), p.getPosted(), p.getPetDesc(),
                    p.getPrice(), p.getQuantity(), photos);
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public List<PetWithPhotosDto> getPetsWithImage() throws PetNotFoundException{
        List<PetWithPhotosDto> pets = dao.getPetsWithPhotos();
        if(pets != null && !pets.isEmpty()){
            return pets;
        }
        else{
            throw new PetNotFoundException();
        }
    }


    public Pet updateQuantity( Long id, Pet pet)
            throws BaseException{
        Pet updatedPet = getById(id);
        updatedPet.setQuantity(pet.getQuantity());
        dao.updateQuantity(updatedPet);
        return updatedPet;
    }

    private void validateInput(Pet pet) throws BaseException{
        if(pet.getGender() == null || pet.getGender().equals("") || pet.getAge() < 0 || pet.getAge() > 20
                || pet.getBreed() == null || pet.getBreed().equals("") ||  pet.getSubBreed().equals("")
                || pet.getSubBreed() == null || pet.getQuantity() > 20
                || pet.getQuantity() < 1 || pet.getPrice() < 0 || (!pet.getGender().equals("M")
                && !pet.getGender().equals("F"))){
            throw new InvalidInputException("Invalid input.");
        }
        if(dao.getByBreeds(pet.getBreed(), pet.getSubBreed()) != null){
            throw new InvalidInputException("This breed and sub breed already exist.");
        }
    }
}
