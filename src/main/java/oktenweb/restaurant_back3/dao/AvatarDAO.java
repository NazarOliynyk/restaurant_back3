package oktenweb.restaurant_back3.dao;

import oktenweb.restaurant_back3.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AvatarDAO extends JpaRepository<Avatar, Integer>{

    List<Avatar> findByRestaurantEmail(String restaurantEmail);
    List<Avatar> findByRestaurantId(int id);
}
