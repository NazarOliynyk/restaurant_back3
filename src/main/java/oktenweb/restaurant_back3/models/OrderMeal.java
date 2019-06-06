package oktenweb.restaurant_back3.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"meals"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    Date date;
    String reasonOfCancelation;
    String descriptionFromClient;
    String descriptionFromRestaurant;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    TypeOfResponse responseFromClient;

    @Enumerated(EnumType.STRING)
    TypeOfResponse responseFromRestaurant;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Client client;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Restaurant restaurant;

    @JsonIgnore
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.EAGER
            //mappedBy = "orders"
    )
    List<Meal> meals = new ArrayList<>();

}
