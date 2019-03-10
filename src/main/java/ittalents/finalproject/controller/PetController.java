package ittalents.finalproject.controller;

import ittalents.finalproject.service.PetService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.PetWithPhotosDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.pets.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/pets")
public class PetController extends BaseController {

    @Autowired
    private PetService service;

    @PostMapping(value = "/add")
    public Pet add(@RequestBody Pet pet, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        return service.add(pet);
    }

    @GetMapping("/filter")
    public List<PetWithPhotosDto> filterAndSort(@RequestParam (name = "sortBy", required = false) String sortBy,
                             @RequestParam(name = "breed", required = false) String breed,
                             @RequestParam(name = "subBreed", required = false) String subBreed,
                             @RequestParam(name = "fromPrice", required = false) Double fromPrice,
                             @RequestParam(name = "toPrice", required = false) Double toPrice,
                             @RequestParam(name = "gender", required = false) String gender,
                             @RequestParam(name = "fromAge", required = false) Integer fromAge,
                             @RequestParam(name = "toAge", required = false) Integer toAge,
                             @RequestParam(name = "postedAfter", required = false) Timestamp postedAfter) {

        return service.filterAndSort(sortBy, breed, subBreed, fromPrice, toPrice, gender, fromAge, toAge, postedAfter);
    }

    @GetMapping()
    public Object getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    public Pet getById(@PathVariable Long id) throws PetNotFoundException{
        return service.getById(id);
    }

    @DeleteMapping(value = "/{id}/remove")
    public Message removeById(@PathVariable Long id, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        validatePetId(id);
        return service.removeById(id);
    }


    @PostMapping("/{id}/images")
    public Photo setPetImage(@PathVariable("id") Long id, @RequestParam MultipartFile img, HttpSession ses) throws BaseException, IOException {
        validateLoginAdmin(ses);
        validatePetId(id);
        return service.setPetImage(id, img, ses);
    }

    @GetMapping("/{id}/images")
    public PetWithPhotosDto getPetImages(@PathVariable Long id)throws PetNotFoundException{
        validatePetId(id);
        return service.getPetImages(id);
    }

    @GetMapping("/images")
    public List<PetWithPhotosDto> getPetsWithImage() throws PetNotFoundException{
        return service.getPetsWithImage();
    }

    @PutMapping(value = "/{id}")
    public Pet updateQuantity(@PathVariable Long id, @RequestBody Pet pet, HttpSession session)
            throws BaseException{
        validateLoginAdmin(session);
        validatePetId(id);
        return service.updateQuantity(id, pet);
    }


    public void validatePetId(Long id) throws PetNotFoundException{
        if(id == null || getById(id) == null){
            throw new PetNotFoundException();
        }
    }

}
