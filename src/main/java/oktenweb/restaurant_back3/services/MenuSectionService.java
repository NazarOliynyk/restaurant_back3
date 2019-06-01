package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.dao.MenuSectionDAO;
import oktenweb.restaurant_back3.models.MenuSection;
import oktenweb.restaurant_back3.models.ResponseTransfer;
import oktenweb.restaurant_back3.models.Restaurant;
import oktenweb.restaurant_back3.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuSectionService {
    @Autowired
    MenuSectionDAO menuSectionDAO;
    @Autowired
    UserServiceImpl userServiceImpl;

    public List<MenuSection> findAllByRestaurantId(int id){
        return menuSectionDAO.findByRestaurantId(id);
    }

    public ResponseTransfer saveMenuSection(MenuSection menuSection){

        menuSectionDAO.save(menuSection);
        return new ResponseTransfer("Menu section saved successfully");
    }

    public MenuSection findById(int id){
        return menuSectionDAO.getOne(id);
    }

    public MenuSection findByNameAndRestaurant(String name, Restaurant restaurant){
        return menuSectionDAO.findByNameAndRestaurant(name, restaurant);
    }

    public ResponseTransfer deleteMenuSection(int id){
        MenuSection menuSection = menuSectionDAO.getOne(id);
        Restaurant restaurant = menuSection.getRestaurant();
        List<MenuSection> menuSections = restaurant.getMenuSections();
        menuSections.remove(menuSection);
        restaurant.setMenuSections(menuSections);
        //userServiceImpl.save(restaurant);
        menuSectionDAO.delete(menuSection);
        return new ResponseTransfer("Menu Section was deleted");
    }
}
