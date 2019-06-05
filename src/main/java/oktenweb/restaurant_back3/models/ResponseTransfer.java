package oktenweb.restaurant_back3.models;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResponseTransfer {

    String text;
    double eur;
    double usd;
    double pln;

    public ResponseTransfer(String text) {
        this.text = text;
    }

    public ResponseTransfer( double eur, double usd, double pln) {
        this.eur = eur;
        this.usd = usd;
        this.pln = pln;
    }
}
