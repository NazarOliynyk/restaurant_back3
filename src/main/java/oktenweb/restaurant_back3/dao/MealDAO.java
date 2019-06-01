package oktenweb.restaurant_back3.dao;

import oktenweb.restaurant_back3.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDAO extends JpaRepository<Meal, Integer> {

    List<Meal> findByRestaurantEmail(String restaurantEmail);
    List<Meal> findByMenuSectionName(String menuSectionName);
    List<Meal> findByRestaurantId(int id);
}
