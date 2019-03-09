package ittalents.finalproject.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.ImageCannotBeAddedException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/pets")
public class PetController extends BaseController {

    @Autowired
    private PetDao dao;

    @Autowired
    private ImageController imgContr;

    //validation for existing id

    @PostMapping(value = "/add")
    public Pet add(@RequestBody Pet pet, HttpSession session) throws BaseException {
        validateInput(pet);
        super.validateLoginAdmin(session);
        System.out.println(pet.toString());
        return dao.addPet(pet);
    }

//    @GetMapping("/filter")
//    public Pet getByBreedSubBreed(@RequestParam String breed, @RequestParam String subBreed) throws PetNotFoundException{
//        Pet pet = null;
//        if(breed != null && !breed.isEmpty() &&
//           subBreed != null && !subBreed.isEmpty()) {
//            pet = dao.getByBreeds(breed, subBreed);
//        }
//        if(pet != null){
//            return pet;
//        }
//        else {
//            throw new PetNotFoundException();
//        }
//    }

    @GetMapping()
    public Object getAll() {
        List<Pet> pets = dao.getAll();
        if(pets.isEmpty()){
            return new Message("There are no pets for sale", LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
        }
        else{
            return pets;
        }
    }

    @GetMapping(value = "/{id}")
    public Pet getById(@PathVariable long id) throws PetNotFoundException{
        Pet pet = null;
        if(id > 0){
            pet = dao.getById(id);
        }
        if(pet != null){
            return pet;
        }
        else{
            throw new PetNotFoundException();
        }
    }

    @DeleteMapping(value = "/{id}/remove")
    public Message removeById(@PathVariable long id, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        if(id > 0 && getById(id) != null) {
            dao.delete(id);
            return new Message("Successfully deleted pet!", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else{
            throw new PetNotFoundException();
        }
    }


    @PostMapping("/{id}/images")
    public Message setPetImage(@PathVariable("id") long id, @RequestParam MultipartFile img, HttpSession ses) throws BaseException, IOException {
        validateLoginAdmin(ses);
        if(!img.isEmpty() && id > 0 && getById(id) != null) {
            String imgTitle = imgContr.uploadImage(img, ses);
            Photo photo = new Photo(imgTitle, id);
            dao.setImage(photo);
            return new Message("The photo added successfully", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else{
            throw new ImageCannotBeAddedException();
        }
    }

    @GetMapping("/{id}/images")
    public PetWithPhotosDto getPetImages(@PathVariable long id)throws PetNotFoundException{
        Pet p = this.getById(id);
        List<Photo> photos = dao.getImagesById(id);
        PetWithPhotosDto pet = new PetWithPhotosDto(p.getId(), p.getGender(),
                    p.getBreed(), p.getSubBreed(), p.getAge(), p.getPosted(), p.getPetDesc(),
                    p.isInSale(), p.getPrice(), p.getQuantity(), photos);
        return pet;
    }

    @GetMapping("/images")
    public List<PetWithPhotosDto> getPetsWithImage() throws PetNotFoundException{
        List<PetWithPhotosDto> pets = dao.getPetsWithPhotos();
        if(pets != null && !pets.isEmpty()){
            return pets;
        }
        else{
            throw new PetNotFoundException();
        }
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
