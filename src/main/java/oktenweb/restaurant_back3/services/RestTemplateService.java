package oktenweb.restaurant_back3.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oktenweb.restaurant_back3.models.ResponseTransfer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

@Service
public class RestTemplateService {

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();

    public ResponseTransfer read() throws IOException {


        String dateSubstring =
                String.valueOf(new Date().toInstant()).substring(0, 10);

        String urlFixer
                = "http://data.fixer.io/api/" + dateSubstring +
                "?access_key=229d1da7b736ef77d158ea0c224c4344" +
                "&symbols=USD,EUR,PLN,UAH";
        ResponseEntity<String> response
                = restTemplate.getForEntity(urlFixer , String.class);
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode rates = root.path("rates");

        double eur = 1/Double.parseDouble(rates.path("UAH").asText());
        double usd = eur*Double.parseDouble(rates.path("USD").asText());
        double pln = eur*Double.parseDouble(rates.path("PLN").asText());

        return new ResponseTransfer(eur, usd, pln);
    }
}
