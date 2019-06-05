package oktenweb.restaurant_back3.controllers;

import oktenweb.restaurant_back3.models.*;
import oktenweb.restaurant_back3.services.AvatarService;
import oktenweb.restaurant_back3.services.MealService;
import oktenweb.restaurant_back3.services.MenuSectionService;
import oktenweb.restaurant_back3.services.OrderMealService;
import oktenweb.restaurant_back3.services.impl.UserServiceImpl;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.util.*;

@RestController
public class MainController {

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    MenuSectionService menuSectionService;
    @Autowired
    MealService mealService;
    @Autowired
    OrderMealService orderMealService;
    @Autowired
    AvatarService avatarService;

    @CrossOrigin(origins = "*")
    @PostMapping("/saveRestaurant")
    public ResponseTransfer saveRestaurant(@RequestBody Restaurant restaurant){
        ResponseTransfer response = userServiceImpl.save(restaurant);
        System.out.println("Controller: "+response.getText());
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveClient")
    public ResponseTransfer saveClient(@RequestBody Client client){
        ResponseTransfer response = userServiceImpl.save(client);
        System.out.println("Controller: "+response.getText());
        return response;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/verification/{jwt}")
    public String verification(@PathVariable String jwt){

        return userServiceImpl.verification(jwt);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateRestaurant")
    public ResponseTransfer updateRestaurant(@RequestBody Restaurant restaurant){

        return userServiceImpl.update(restaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateClient")
    public ResponseTransfer updateClient(@RequestBody Client client){

        return userServiceImpl.update(client);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changePasswordRestaurant")
    public ResponseTransfer changePasswordRestaurant(@RequestBody Restaurant restaurant){

        return userServiceImpl.changePassword(restaurant);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changePasswordClient")
    public ResponseTransfer changePasswordClient(@RequestBody Client client){

        return userServiceImpl.changePassword(client);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/checkPassword/{id}")
    public ResponseTransfer checkPassword(
            @PathVariable("id") int id,
            @RequestBody String password){

        return userServiceImpl.checkPassword(id, password);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/findRestaurant/{id}")
    public Restaurant findRestaurant(@PathVariable("id") int id){
        System.out.println("id: "+id);
        return (Restaurant) userServiceImpl.findOneById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/findClient/{id}")
    public Client findClient(@PathVariable("id") int id){

        return (Client) userServiceImpl.findOneById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getLogins")
    public List<User> getLogins(){

        return userServiceImpl.getLogins();
    }

    @CrossOrigin(origins = "*")
    @Transactional
    @PostMapping("/forgotPassword/{id}")
    public ResponseTransfer forgotPassword(@PathVariable("id") int id,
                                           @RequestBody User u){
        System.out.println("u.toString(): "+u.toString());

        userServiceImpl.setTimeout(() ->{
            System.out.println("Tymeout Works");
            userServiceImpl.setRandomPassIfNotChanged(id);}, 180000);
        return userServiceImpl.setRandomPass(id);


    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseTransfer deleteUser(@PathVariable("id") int id) {

        return userServiceImpl.deleteById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRestaurants")
    public List<Restaurant> getRestaurants(){
        List<User> users = userServiceImpl.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (User user: users) {
            if(user.getClass().equals(Restaurant.class)){
                restaurants.add((Restaurant) user);
            }
        }
        return restaurants;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getClients")
    public List<Client> getClients(){
        List<User> users = userServiceImpl.findAll();
        List<Client> clients = new ArrayList<>();
        for (User user: users) {
            if(user.getClass().equals(Client.class)){
                clients.add((Client) user);
            }
        }
        return clients;
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/getMenuSections/{id}")
    public List<MenuSection>  getMenuSections
            (@PathVariable("id") int id){
        System.out.println("id: "+id);
        return menuSectionService.findAllByRestaurantId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getMeals/{id}")
    public List<Meal> getMeals
            (@PathVariable("id") int id){

        return mealService.findAllByRestaurantId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getClientOrders/{id}")
    public List<OrderMeal> getClientOrders
            (@PathVariable("id") int id){
        return orderMealService.findAllByClientId(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRestaurantOrders/{id}")
    public List<OrderMeal> getRestaurantOrders
            (@PathVariable("id") int id){
        return orderMealService.findAllByRestaurantId(id);
    }
}
