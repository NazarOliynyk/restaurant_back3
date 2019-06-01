package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.dao.MealDAO;
import oktenweb.restaurant_back3.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    @Autowired
    MealDAO mealDAO;

    public List<Meal> findAllByRestaurantEmail(Restaurant restaurant){
        return mealDAO.findByRestaurantEmail(restaurant.getEmail());
    }

    public List<Meal> findAllByRestaurantId(int id){
        return mealDAO.findByRestaurantId(id);
    }

    public List<Meal> findAllByMenuSectionName(MenuSection menuSection){
        return mealDAO.findByMenuSectionName(menuSection.getName());
    }

    public ResponseTransfer saveMeal( Meal meal) {

        mealDAO.save(meal);
        return new ResponseTransfer("Meal saved successfully!");
    }

    public Meal findOne(int id){
        return mealDAO.getOne(id);
    }

    public ResponseTransfer deleteMeal(int id){

        Meal meal = mealDAO.getOne(id);
        Restaurant restaurant = meal.getRestaurant();
        List<Meal> mealsOfRestaurant = restaurant.getMeals();
        mealsOfRestaurant.remove(meal);
        restaurant.setMeals(mealsOfRestaurant);

        MenuSection menuSection = meal.getMenuSection();
        List<Meal> mealsOfMenuSection = menuSection.getMeals();
        mealsOfMenuSection.remove(meal);
        menuSection.setMeals(mealsOfMenuSection);

        List<OrderMeal> orders = meal.getOrders();
        for (OrderMeal order : orders) {
            List<Meal> mealsOfOrder = order.getMeals();
            mealsOfOrder.remove(meal);
            order.setMeals(mealsOfOrder);
        }
        mealDAO.delete(meal);
        return new ResponseTransfer("Meal was deleted");
    }

}
