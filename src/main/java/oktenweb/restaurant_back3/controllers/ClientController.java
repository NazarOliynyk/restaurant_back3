package oktenweb.restaurant_back3.controllers;

import oktenweb.restaurant_back3.models.Meal;
import oktenweb.restaurant_back3.models.OrderMeal;
import oktenweb.restaurant_back3.models.ResponseTransfer;
import oktenweb.restaurant_back3.services.OrderMealService;
import oktenweb.restaurant_back3.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    OrderMealService orderMealService;



    @CrossOrigin(origins = "*")
    @PostMapping("/saveOrder/{id}")
    public ResponseTransfer saveOrder(@PathVariable("id") int id,
                                      @RequestBody List<Integer> ids){
        System.out.println("/saveOrder/{id}: "+id);
        for (Integer integer : ids) {
            System.out.println("id of a meal: "+integer);
        }
        return orderMealService.saveOrder(id, ids);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteOrder/{id}")
    public ResponseTransfer deleteOrder(@PathVariable int id){

        return orderMealService.deleteOrderByClient(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getMealsOfOrder/{id}")
    public List<Meal> getMealsOfOrder(@PathVariable int id){
        return orderMealService.getMealsOfOrder(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cancelOrderByClient/{id}")
    public ResponseTransfer cancelOrderByClient(@PathVariable("id") int id,
                                                @RequestBody String reasonOfCancelation){

        return orderMealService.cancelOrderByClient(id, reasonOfCancelation);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/confirmOrderServed/{id}")
    public ResponseTransfer confirmOrderServed(@PathVariable("id") int id,
                                               @RequestBody String s){
        System.out.println(s);
        return orderMealService.confirmOrderServed(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/negativeFromClient/{id}")
    public ResponseTransfer negativeFromClient(@PathVariable("id") int id,
                                               @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.negativeFromClient(orderMeal, descriptionFromClient);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/positiveFromClient/{id}")
    public ResponseTransfer positiveFromClient(@PathVariable("id") int id,
                                               @RequestBody String descriptionFromClient){

        OrderMeal orderMeal = orderMealService.findById(id);
        return orderMealService.positiveFromClient(orderMeal, descriptionFromClient);
    }
}
