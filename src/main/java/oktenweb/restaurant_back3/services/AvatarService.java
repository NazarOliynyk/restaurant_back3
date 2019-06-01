package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.dao.AvatarDAO;
import oktenweb.restaurant_back3.models.Avatar;
import oktenweb.restaurant_back3.models.ResponseTransfer;
import oktenweb.restaurant_back3.models.Restaurant;
import oktenweb.restaurant_back3.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvatarService {

    @Autowired
    AvatarDAO avatarDAO;
    @Autowired
    UserServiceImpl userServiceImpl;

    public List<Avatar> findByRestaurantId(int id){
        return avatarDAO.findByRestaurantId(id);
    }


    public List<File> findFilesByRestaurantId(int id){
        List<Avatar> avatars = avatarDAO.findByRestaurantId(id);
        List<File> files = new ArrayList<>();
        for (Avatar avatar : avatars) {
            File file =  new File("/ava/"+avatar.getImage());
            files.add( file);
        }

        return files;
    }

    //    private String path =  "D:\\FotoSpringRestaurantBackEnd2Rest"+ File.separator;
    private String path =  "D:\\AngularProjects\\restaurantfrontend2\\src\\assets\\images"+ File.separator;

    // file:///D:/AngularProjects/restaurantfrontend2/src/assets/images/

    public ResponseTransfer saveAvatar
            (int restaurantId, MultipartFile image){


        try {
            image.transferTo(new File(path + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Failed to add an image");
        }
        Avatar avatar = new Avatar();
        Restaurant restaurant = (Restaurant) userServiceImpl.findOneById(restaurantId);
        avatar.setRestaurant(restaurant);
        avatar.setImage(image.getOriginalFilename());
        avatarDAO.save(avatar);
        return new ResponseTransfer("Image saved");
    }

//    public ResponseTransfer deleteAvatar(int id){
//        Avatar avatar = avatarDAO.getOne(id);
//
//        Path pathToFile = FileSystems.getDefault().getPath(path + avatar.getImage());
//        try {
//            Files.delete(pathToFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseTransfer("Image was not deleted");
//        }
//
//        Restaurant restaurant = avatar.getRestaurant();
//        List<Avatar> avatars = restaurant.getAvatars();
//        avatars.remove(avatar);
//        restaurant.setAvatars(avatars);
//        avatarDAO.delete(avatar);
//        return new ResponseTransfer("Image was deleted");
//    }

}
