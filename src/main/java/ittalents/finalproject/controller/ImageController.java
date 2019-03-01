package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ImageUploadDto;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.util.Base64;

@RestController
@RequestMapping(value = "/images")
public class ImageController extends BaseController {

    private static final String IMAGE_DIR = "D:\\uploads\\";

    @PostMapping()
    public void uploadImage(@RequestBody ImageUploadDto dto, HttpSession session) throws IOException, BaseException {
        User user = (User) session.getAttribute("loggedUser");
//        super.validateLoginAdmin(session);
        String base64 = dto.getImageStr();
        byte[] bytes = Base64.getMimeDecoder().decode(base64);
        Long id = user.getId();
        String fileName = id.intValue() + System.currentTimeMillis() + ".png";
        File file = new File(IMAGE_DIR + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        //in petController and ProductController setting the images!!
    }

//    @GetMapping(value = "/{image_path}", produces = "image/png")
//    public @ResponseBody byte[] downloadImage(@PathVariable String img) throws FileNotFoundException {
//          File newFile = new File(IMAGE_DIR + img);
//          FileInputStream fis = new FileInputStream(newFile);
//        return fis.();//not working
//    }
}
