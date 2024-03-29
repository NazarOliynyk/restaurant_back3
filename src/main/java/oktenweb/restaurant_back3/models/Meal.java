package oktenweb.restaurant_back3.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Meals")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"restaurant", "menuSection"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    String quantity;
    double price;
    String avatar = "";

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Restaurant restaurant;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    MenuSection menuSection;

    public String getMsName() {
        return this.getMenuSection().getName();
    }

    @JsonIgnore
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.EAGER,
            mappedBy = "meals"
    )
    List<OrderMeal> orders = new ArrayList<>();

}
