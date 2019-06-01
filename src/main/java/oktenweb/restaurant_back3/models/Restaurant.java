package oktenweb.restaurant_back3.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"meals", "menuSections", "orders", "avatars"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@DiscriminatorValue("RESTAURANT")

public class Restaurant extends User{

    String name;
    String address;
    // String email;
    String phoneNumber;
    String additionalInfo;
    int restaurantPositiveResponses;
    int restaurantNegativeResponses;


    @JsonIgnore
    @OneToOne(mappedBy = "restaurant")
    private Avatar avatar;


    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<Meal> meals = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<MenuSection> menuSections = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant"
    )
    List<OrderMeal> orders = new ArrayList<>();

//    @JsonIgnore
//    @ManyToMany(
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//
//    )
//    List<Client> clients = new ArrayList<>();


}
