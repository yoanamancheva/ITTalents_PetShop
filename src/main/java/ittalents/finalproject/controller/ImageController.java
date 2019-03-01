package ittalents.finalproject.controller;

import com.mysql.cj.util.Base64Decoder;
import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ImageUploadDto;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

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
        User user = (User) session.getAttribute(BaseController.LOGGED_USER);
        super.validateLoginAdmin(session);
        String base64 = dto.getImageStr();
        byte[] bytes = Base64.getDecoder().decode(base64);
        Long id = user.getId();
        String fileName = id.intValue() + System.currentTimeMillis() + ".png";
        File file = new File(IMAGE_DIR + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
//        in petController and ProductController setting the images!!
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
