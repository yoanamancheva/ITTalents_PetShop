package ittalents.finalproject.controller;

import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.model.pojos.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;

@RestController
@RequestMapping(value = "/images")
public class ImageController extends BaseController {

    private static final String IMAGE_DIR = "D:" + File.separator + "uploads" + File.separator;

    @PostMapping()
    public String uploadImage(@RequestParam MultipartFile img, HttpSession session) throws BaseException, IOException {
        if(!img.isEmpty()) {
            User user = (User) session.getAttribute(BaseController.LOGGED_USER);
            super.validateLoginAdmin(session);
            byte[] bytes = img.getBytes();
            Long id = user.getId();
            String fileName = id.intValue() + System.currentTimeMillis() + ".png";
            File imgDir = new File(IMAGE_DIR);
            if(!imgDir.exists()){
                imgDir.mkdirs();
            }
            File file = new File(IMAGE_DIR + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();

            return fileName;
        }
        throw new InvalidInputException("Image not valid");
    }

    @GetMapping(value = "/{image_path}", produces = "image/png")
    public @ResponseBody byte[] downloadImage(@PathVariable("image_path") String img) throws IOException {
          File newFile = new File(IMAGE_DIR + img);
          FileInputStream fis = new FileInputStream(newFile);
          byte[] bytes = new byte[(int)newFile.length()];
          fis.read(bytes);
          fis.close();
          return bytes;
    }
}
