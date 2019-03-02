package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.ErrorMsg;
import ittalents.finalproject.model.pojos.dto.ImageUploadDto;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/pets")
public class PetController extends BaseController {

    @Autowired
    private PetDao dao;

    @Autowired
    private ImageController imgContr;

    @PostMapping(value = "/add")
    public @ResponseBody ErrorMsg add(@RequestBody Pet pet, HttpSession session) throws BaseException {

        if(pet.getGender() == null || pet.getAge() < 0 || pet.getBreed() == null || pet.getSubBreed() == null ||
                pet.getPetDesc() == null || pet.getQuantity() < 1 || pet.getPrice() < 0 || pet.getGender().equals("M") ||
                !pet.getGender().equals("F")){
            throw new InvalidInputException("Invalid input.");
        }
        super.validateLoginAdmin(session);
        dao.addPet(pet);
        System.out.println(pet.toString());
        return new ErrorMsg("The pet with id " + pet.getId() + " was successfully added", LocalDateTime.now(), 200);
    }

    @GetMapping()
    public @ResponseBody List<Pet> getAll(){
        return dao.getAll();
    }

    @GetMapping(value = "/{id}")
    public Pet getById(@PathVariable long id){
        return dao.getById(id);
    }

//    @GetMapping(value = "/{breed}")
//    public List<Pet> getByBreed(@RequestParam String breed){
//        return dao.getFiltred(breed, null, -1, -1, null, -1,
//                -1, null, null);
//    }

//    @GetMapping(value = "/{breed}/{subBreed}")
//    public List<Pet> getByBreedSubBreed(@PathVariable String breed, @PathVariable String subBreed){
//        return dao.getFiltred(breed, subBreed);
//    }


    @PutMapping(value = "/remove/{id}")
    public @ResponseBody ErrorMsg removeById(@PathVariable long id, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        dao.delete(id);
        return new ErrorMsg("Successfully deleted pet!", LocalDateTime.now(), 200);
    }

    @PostMapping("/{id}/images")
    public @ResponseBody ErrorMsg setPetImage(@PathVariable long petId, @RequestBody ImageUploadDto img, HttpSession ses) throws BaseException, IOException {
        validateLoginAdmin(ses);
        imgContr.uploadImage(img, ses);
        Photo photo = new Photo(img.getTitle(), petId);
        dao.setImage(photo);
        return new ErrorMsg("The photo added successfully", LocalDateTime.now(), 200);
    }

    @GetMapping("/{id}/images")
    public @ResponseBody PetWithPhotosDto getPetImages(@PathVariable long id){
        Pet p = this.getById(id);
        List<Photo> photos = dao.getImagesById(id);
        PetWithPhotosDto pet = new PetWithPhotosDto(p.getId(), p.getGender(), p.getBreed(), p.getSubBreed(), p.getAge(), p.getPosted(), p.getPetDesc(),
                    p.isInSale(), p.getPrice(), p.getQuantity(), photos);
        return pet;
    }

    @GetMapping("/images")
    public @ResponseBody List<PetWithPhotosDto> getPetsWithImage(){
        return dao.getPetsWithPhotos();
    }

}
