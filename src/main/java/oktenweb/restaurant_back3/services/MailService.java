package oktenweb.restaurant_back3.services;

public interface MailService {
    String send(String email, String message, String subject);
}
