package ittalents.finalproject.service;

import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ImageService {


    private static final String IMAGE_DIR = "D:" + File.separator + "uploads" + File.separator;


    public String uploadImage(MultipartFile img, User user) throws BaseException, IOException {
        if(!img.isEmpty()) {
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


    public byte[] downloadImage(String img) throws IOException {
        File newFile = new File(IMAGE_DIR + img);
        FileInputStream fis = new FileInputStream(newFile);
        byte[] bytes = new byte[(int)newFile.length()];
        fis.read(bytes);
        fis.close();
        return bytes;
    }
}
