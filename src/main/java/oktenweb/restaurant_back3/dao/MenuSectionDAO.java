package oktenweb.restaurant_back3.dao;

import oktenweb.restaurant_back3.models.MenuSection;
import oktenweb.restaurant_back3.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuSectionDAO extends JpaRepository<MenuSection, Integer>{

    List<MenuSection> findByRestaurantId(int id);
    MenuSection findByNameAndRestaurant(String name, Restaurant restaurant);

}
