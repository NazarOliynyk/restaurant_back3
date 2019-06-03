package oktenweb.restaurant_back3.services;

import oktenweb.restaurant_back3.models.ResponseTransfer;
import oktenweb.restaurant_back3.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseTransfer save(User user);

    List<User> findAll();

    User findOneById(int id);

    ResponseTransfer deleteById(int id);
}
