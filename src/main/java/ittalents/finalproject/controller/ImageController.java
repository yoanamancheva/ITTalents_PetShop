package ittalents.finalproject.controller;

import ittalents.finalproject.service.ImageService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.model.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;

@RestController
@RequestMapping(value = "/images")
public class ImageController extends BaseController {

    @Autowired
    private ImageService service;

    @PostMapping()
    public String uploadImage(@RequestParam MultipartFile img, HttpSession session) throws BaseException, IOException {
        User user = (User) session.getAttribute(BaseController.LOGGED_USER);
        super.validateLoginAdmin(session);
        return service.uploadImage(img, user);
    }

    @GetMapping(value = "/{image_path}", produces = "image/png")
    public byte[] downloadImage(@PathVariable("image_path") String img) throws IOException {
          return service.downloadImage(img);
    }
}
