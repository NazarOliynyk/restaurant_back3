package oktenweb.restaurant_back3.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import oktenweb.restaurant_back3.dao.UserDAO;
import oktenweb.restaurant_back3.models.*;
import oktenweb.restaurant_back3.services.OrderMealService;
import oktenweb.restaurant_back3.services.UserService;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    MailServiceImpl mailServiceImpl;
    @Autowired
    OrderMealService orderMealService;

    @Override
    public ResponseTransfer save(User user) {

        if (user.getUsername().equals("admin") || userDAO.existsByUsername(user.getUsername())) {
            return new ResponseTransfer("User with such login already exists!!");
        } else if(userDAO.existsByEmail(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {
            String jwtoken = Jwts.builder()
                    .setSubject(user.getEmail())
                    .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + 200000))
                    .compact();
            String responseFromMailSender =
                    mailServiceImpl.send(user.getEmail(),
                            "http://localhost:8080/verification/" + jwtoken,
                            "Confirm registration");
            if(responseFromMailSender.equals("Message was sent")){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userDAO.save(user);
                return new ResponseTransfer("Preliminary Registration is completed. Take a look into your email");
            }else {
                return new ResponseTransfer(responseFromMailSender);
            }
        }
    }

    public String verification(String jwt) {
        String email;
        try {
            email = Jwts.parser().
                    setSigningKey("yes".getBytes()).
                    parseClaimsJws(jwt).getBody().getSubject();

        }catch (MalformedJwtException e){
            System.out.println(e.toString());
            return "Verification failed";
        }
        User user = userDAO.findByEmail(email);

        if(user == null){
            return "Verification failed";
        }else {
            user.setEnabled(true);
            userDAO.save(user);
            return "Verification successful! Go To Log In page";
        }
    }



    public ResponseTransfer update(User user) {
        User userBeforeUpdate = userDAO.getOne(user.getId());
        List<String> emails = new ArrayList<>();
        List<User> users = userDAO.findAll();
        for (User user1 : users) {
            emails.add(user1.getEmail());
        }
        emails.remove(userBeforeUpdate.getEmail());
        if(emails.contains(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {
            user.setEnabled(true);
            userDAO.save(user);
            return new ResponseTransfer("User has been updated successfully.");
        }
    }

//
//    @Override
//    public ResponseTransfer deleteById(int id){
//
//        User user = userDAO.findById(id);
//
//        if(user.getClass().equals(Client.class)){
//            userDAO.deleteById(id);
//            return new ResponseTransfer("User was deleted successfully");
//        }else   {
//            Restaurant restaurant = (Restaurant) userDAO.findById(id);
//            if(!restaurant.getAvatar().equals("")){
//                String path =
//                        "D:\\Restaurants3\\restaurantsfront3\\src\\assets\\images"+ File.separator;
//                Path pathToFile =
//                        FileSystems.getDefault().getPath(path + restaurant.getAvatar());
//                try {
//                    Files.delete(pathToFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return new ResponseTransfer("Image was not deleted");
//                }
//            }
//            userDAO.deleteById(id);
//            return new ResponseTransfer("User was deleted successfully");
//        }
//
//    }

@Override
public ResponseTransfer deleteById(int id){

    User user = userDAO.findById(id);

    if(user.getClass().equals(Client.class)){

        List<OrderMeal> orderMeals = orderMealService.findAllByClientId(id);
        List<Restaurant> restaurants = new ArrayList<>();

        orderMeals.forEach(orderMeal -> restaurants.add(orderMeal.getRestaurant()));

        orderMeals.forEach(om -> orderMealService.deleteOrder(om));

        userDAO.deleteById(id);
        restaurants.forEach(restaurant -> orderMealService.recountRestaurantResponses(restaurant));
        return new ResponseTransfer("User was deleted successfully");

    }else   {
        Restaurant restaurant = (Restaurant) userDAO.findById(id);
        List<OrderMeal> orderMeals = orderMealService.findAllByRestaurantId(id);
        List<Client> clients = new ArrayList<>();
        if(!restaurant.getAvatar().equals("")){
            String path =
                    "D:\\Restaurants3\\restaurantsfront3\\src\\static\\images"+ File.separator;
            Path pathToFile =
                    FileSystems.getDefault().getPath(path + restaurant.getAvatar());
            try {
                Files.delete(pathToFile);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseTransfer("Image was not deleted");
            }
        }
        orderMeals.forEach(orderMeal -> clients.add(orderMeal.getClient()));
        orderMeals.forEach(om -> orderMealService.deleteOrder(om));

        userDAO.deleteById(id);
        clients.forEach(client -> orderMealService.recountClientResponses(client));

        return new ResponseTransfer("User was deleted successfully");
    }

}


    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findOneById(int id) {
        return userDAO.findById(id);
    }

    // beacause  UserService extends UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }

    // the next few methods are for changing password and getting a new one:

    public ResponseTransfer checkPassword(int id, String password){

        User user = userDAO.getOne(id);
        if(passwordEncoder.matches(password, user.getPassword())){
            return new ResponseTransfer("PASSWORD MATCHES");
        }else {
            return new ResponseTransfer("PASSWORD DOES NOT MATCH");
        }
    }

    public ResponseTransfer changePassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userDAO.save(user);
        return new ResponseTransfer("Password was changed successfully.");
    }

    public List<User> getLogins(){
        List<User> users = userDAO.findAll();
        List<User> logins = new ArrayList<>();
        for (User u: users) {
            User user = new User();
            user.setId(u.getId());
            user.setUsername(u.getUsername());
            user.setEmail(u.getEmail());
            logins.add(user);
        }
        return logins;
    }

    // https://stackoverflow.com/questions/4044726/how-to-set-a-timer-in-java?noredirect=1&lq=1
    // https://stackoverflow.com/questions/26311470/what-is-the-equivalent-of-javascript-settimeout-in-java
    public void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    private String randomPass;

    public ResponseTransfer setRandomPass(int id){
        User user = this.findOneById(id);
        Random r = new Random ();
        int random = r.nextInt(9999);
        randomPass = String.valueOf(random);
        user.setPassword(randomPass);
        System.out.println(random);
        System.out.println(randomPass);
        String emailPassChanged = "<div>\n" +
                "    <a href=\"http://localhost:4200\" target=\"_blank\"> Your password was changed to: " +
                "</a>" + "</div>";
        String responseFromMailSender =
                mailServiceImpl.send(user.getEmail(),
                        emailPassChanged +randomPass,
                        "Temporary password");
        if(responseFromMailSender.equals("Message was sent")){
            return this.changePassword(user);
        }else {
            return new ResponseTransfer(responseFromMailSender);
        }
    }

    public void setRandomPassIfNotChanged(int id){
        User user = this.findOneById(id);
        if(this.checkPassword(id, randomPass).getText().equals("PASSWORD MATCHES")){
            Random r = new Random ();
            int random = r.nextInt(9999);
            randomPass = String.valueOf(random);
            user.setPassword(randomPass);
            this.changePassword(user);
        }
    }

}
