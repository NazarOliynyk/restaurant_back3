package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.dao.MealDAO;
import oktenweb.restaurant_back3.dao.UserDAO;
import oktenweb.restaurant_back3.models.Meal;
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
    UserDAO userDAO;
    @Autowired
    MealDAO mealDAO;

//    private String path =
//            "D:\\Restaurants3\\restaurantsfront3\\src\\assets\\images"+ File.separator;

    private String path =
            System.getProperty("user.home") + File.separator +
                    "Restaurant3_images" + File.separator;

    public ResponseTransfer saveAvatarToRestaurant
            (int restaurantId, MultipartFile image){

        try {
            image.transferTo(new File(path + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Failed to add an image");
        }
        Restaurant restaurant = (Restaurant) userDAO.findById(restaurantId);
        restaurant.setAvatar(image.getOriginalFilename());
        userDAO.save(restaurant);
        return new ResponseTransfer("Image saved");
    }

    public ResponseTransfer saveAvatarToMeal
            (int mealId, MultipartFile image){

        try {
            image.transferTo(new File(path + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Failed to add an image");
        }
        Meal meal = mealDAO.findById(mealId);
        meal.setAvatar(image.getOriginalFilename());
        mealDAO.save(meal);
        return new ResponseTransfer("Image saved");
    }

    public ResponseTransfer deleteAvatarFromRestaurant(int restaurantId){

        Restaurant restaurant = (Restaurant) userDAO.findById(restaurantId);

        Path pathToFile =
                FileSystems.getDefault().getPath(path + restaurant.getAvatar());
        try {
            Files.delete(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Image was not deleted");
        }

        restaurant.setAvatar("");
        userDAO.save(restaurant);
        return new ResponseTransfer("Image was deleted");
    }

    public ResponseTransfer deleteAvatarFromMeal(int mealId){

        Meal meal = mealDAO.findById(mealId);

        Path pathToFile =
                FileSystems.getDefault().getPath(path + meal.getAvatar());
        try {
            Files.delete(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseTransfer("Image was not deleted");
        }

        meal.setAvatar("");
        mealDAO.save(meal);
        return new ResponseTransfer("Image was deleted");
    }
}

