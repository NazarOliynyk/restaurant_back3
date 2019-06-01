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
@ToString(exclude = {"orders"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@DiscriminatorValue("CLIENT")
public class Client extends User{

    // String email;
    int clientPositiveResponses;
    int clientNegativeResponses;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "client"
    )
    List<OrderMeal> orders = new ArrayList<>();

//    @JsonIgnore
//    @ManyToMany(
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY,
//            mappedBy = "clients"
//    )
//    List<Restaurant> restaurants = new ArrayList<>();



}
