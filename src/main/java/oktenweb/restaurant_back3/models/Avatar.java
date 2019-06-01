package oktenweb.restaurant_back3.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name = "Avatars")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "restaurant")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String image;

    @JsonIgnore
    @OneToOne
    private Restaurant restaurant;

    @JsonIgnore
    @OneToOne
    private Meal meal;


}
