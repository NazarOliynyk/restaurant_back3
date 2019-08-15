package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.dao.MealDAO;
import oktenweb.restaurant_back3.dao.OrderMealDAO;
import oktenweb.restaurant_back3.dao.UserDAO;
import oktenweb.restaurant_back3.models.*;
import oktenweb.restaurant_back3.services.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderMealService {

    @Autowired
    OrderMealDAO orderMealDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    MealDAO mealDao;
    @Autowired
    MailServiceImpl mailServiceImpl;

    public List<OrderMeal> findAllByClientId(int id){
        return orderMealDAO.findAllByClientId(id);
    }
    public List<OrderMeal> findAllByRestaurantId(int id){
        return orderMealDAO.findAllByRestaurantId(id);
    }

    public ResponseTransfer saveOrder(int id, List<Integer> ids){
        Client client = (Client) userDAO.findById(id);
        List<Meal> meals = new ArrayList<>();
        for (Integer currentid : ids) {
            Meal meal = mealDao.getOne(currentid);
            meals.add(meal);
        }
        Restaurant restaurant = meals.get(0).getRestaurant();

        OrderMeal orderMeal = new OrderMeal();
        orderMeal.setMeals(meals);
        orderMeal.setRestaurant(restaurant);
        orderMeal.setClient(client);
        orderMeal.setResponseFromRestaurant(TypeOfResponse.NEUTRAL);
        orderMeal.setResponseFromClient(TypeOfResponse.NEUTRAL);
        orderMeal.setOrderStatus(OrderStatus.JUST_ORDERED);
        orderMeal.setDate(new Date());
        orderMealDAO.save(orderMeal);
//        String newOrder = "<div>\n" +
//                "    <a href=\"http://localhost:4200/restaurantOrder\" target=\"_blank\"> You have just got a new order </a>\n" +
//                "</div>";

        String newOrder = "<div>\n" +
                "    <a href=\"http://ec2-18-222-130-33.us-east-2.compute.amazonaws.com:8080/restaurantOrder\" target=\"_blank\"> You have just got a new order </a>\n" +
                "</div>";
        String responseFromMailSender =
                mailServiceImpl.send(restaurant.getEmail(),
                        newOrder,
                        "New order in your List");
        if(responseFromMailSender.equals("Message was sent")){
            return new ResponseTransfer("Order was saved successfully");
        }else {
            return new ResponseTransfer(responseFromMailSender);
        }
    }

    public ResponseTransfer acceptOrderToKitchen(OrderMeal orderMeal){
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Status was changed to IN_PROCESS");
    }
    public OrderMeal findById(int id){
        return orderMealDAO.getOne(id);
    }

    public List<Meal> getMealsOfOrder(int id){
        OrderMeal orderMeal = orderMealDAO.getOne(id);
        return orderMeal.getMeals();
    }

    public Client findClientByOrderId(int id){
        OrderMeal orderMeal = orderMealDAO.getOne(id);
        return orderMeal.getClient();
    }

    // this method is to reduce code in both: deleteOrderByClient and deleteOrderByRestaurant
    private void deleteOrderFromEverywhere(OrderMeal orderMeal){
        Restaurant restaurant = orderMeal.getRestaurant();
        List<OrderMeal> ordersOfRestaurant = restaurant.getOrders();
        ordersOfRestaurant.remove(orderMeal);
        restaurant.setOrders(ordersOfRestaurant);

        Client client = orderMeal.getClient();
        List<OrderMeal> ordersOfClient = client.getOrders();
        ordersOfClient.remove(orderMeal);
        client.setOrders(ordersOfClient);

        List<Meal> meals = orderMeal.getMeals();
        for (Meal meal : meals) {
            List<OrderMeal> ordersOfMeal = meal.getOrders();
            ordersOfMeal.remove(orderMeal);
            meal.setOrders(ordersOfMeal);
        }
    }

    public ResponseTransfer deleteOrderByClient(int id){
        OrderMeal orderMeal = orderMealDAO.getOne(id);
        if(orderMeal.getOrderStatus().equals(OrderStatus.JUST_ORDERED)||
                orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return new ResponseTransfer("Order was deleted");

        }else {
            return new ResponseTransfer
                    ("Can not delete - your order is not just-ordered or canceled by the Restaurant");

        }
    }

    public ResponseTransfer deleteOrderByRestaurant(int id){
        OrderMeal orderMeal = orderMealDAO.getOne(id);
        if(orderMeal.getOrderStatus().equals(OrderStatus.CANCELED_BY_CLIENT)){

            deleteOrderFromEverywhere(orderMeal);

            orderMealDAO.delete(orderMeal);
            return new ResponseTransfer("Order was deleted");

        }else {
            return new ResponseTransfer
                    ("Can not delete - your order is not canceled by the Client");

        }
    }

    public void deleteOrder(OrderMeal orderMeal){
        deleteOrderFromEverywhere(orderMeal);
        orderMealDAO.delete(orderMeal);
    }

    public ResponseTransfer cancelOrderByRestaurant(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.getOne(id);
        orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_RESTAURANT);
        orderMeal.setReasonOfCancelation(reasonOfCancelation);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Status was changed to Canceled by Restaurant");
    }

    public ResponseTransfer cancelOrderByClient(int id, String reasonOfCancelation){

        OrderMeal orderMeal = orderMealDAO.getOne(id);
        if(orderMeal.getOrderStatus().equals(OrderStatus.JUST_ORDERED)){
            orderMeal.setOrderStatus(OrderStatus.CANCELED_BY_CLIENT);
            orderMeal.setReasonOfCancelation(reasonOfCancelation);
            orderMealDAO.save(orderMeal);
            return new ResponseTransfer("Status was changed to Canceled by Client");
        }else {
            return new ResponseTransfer("This order is not Just Ordered. Can not cancel it");
        }

    }

    public ResponseTransfer confirmOrderServed(int id){
        OrderMeal orderMeal = orderMealDAO.getOne(id);
        orderMeal.setOrderStatus(OrderStatus.SERVED);
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Order status - Served");
    }

    public void recountClientResponses(Client client){
       // Client client = order.getClient();
        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: client.getOrders()) {
            if(ord.getResponseFromRestaurant().equals(TypeOfResponse.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromRestaurant().equals(TypeOfResponse.POSITIVE)){
                positive.add(ord);}
        }

        client.setClientNegativeResponses(negative.size());
        client.setClientPositiveResponses(positive.size());
        userDAO.save(client);
    }

    public void recountRestaurantResponses(Restaurant restaurant){

        List<OrderMeal> negative = new ArrayList<>();
        List<OrderMeal> positive = new ArrayList<>();
        for (OrderMeal ord: restaurant.getOrders()) {
            if(ord.getResponseFromClient().equals(TypeOfResponse.NEGATIVE)){
                negative.add(ord);
            }else if(ord.getResponseFromClient().equals(TypeOfResponse.POSITIVE)) {
                positive.add(ord);}
        }

        restaurant.setRestaurantNegativeResponses(negative.size());
        restaurant.setRestaurantPositiveResponses(positive.size());
        userDAO.save(restaurant);
    }

    public ResponseTransfer negativeFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(TypeOfResponse.NEGATIVE);
            recountRestaurantResponses(orderMeal.getRestaurant());
            orderMealDAO.save(orderMeal);
            return new ResponseTransfer("Response changed to negative");

        }else {
            return new ResponseTransfer("Can not change the RESPONSE to your order, change order status to SERVED");
        }
    }

    public ResponseTransfer positiveFromClient(OrderMeal orderMeal, String descriptionFromClient){
        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
                orderMeal.getOrderStatus().equals(OrderStatus.IN_PROCESS)){

            orderMeal.setDescriptionFromClient(descriptionFromClient);
            orderMeal.setResponseFromClient(TypeOfResponse.POSITIVE);
            recountRestaurantResponses(orderMeal.getRestaurant());
            orderMealDAO.save(orderMeal);
            return new ResponseTransfer("Response changed to positive");
        }else {
            return new ResponseTransfer
                    ("Can not change the RESPONSE to your order, change order status to SERVED");
        }
    }

    public ResponseTransfer negativeFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(TypeOfResponse.NEGATIVE);
        recountClientResponses(orderMeal.getClient());
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Response changed to negative");
    }

    public ResponseTransfer positiveFromRestaurant(OrderMeal orderMeal, String descriptionFromRestaurant){
//        if(orderMeal.getOrderStatus().equals(OrderStatus.SERVED)||
//                orderMeal.getOrderStatus().equals(OrderStatus.SERVED))

        orderMeal.setDescriptionFromRestaurant(descriptionFromRestaurant);
        orderMeal.setResponseFromRestaurant(TypeOfResponse.POSITIVE);
        recountClientResponses(orderMeal.getClient());
        orderMealDAO.save(orderMeal);
        return new ResponseTransfer("Response changed to positive");
    }

}
