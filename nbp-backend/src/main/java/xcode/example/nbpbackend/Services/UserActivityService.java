package xcode.example.nbpbackend.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xcode.example.nbpbackend.Repositories.UserActivityRepository;
import xcode.example.nbpbackend.models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivityService implements IUserActivityService {

    private final UserActivityRepository userActivityRepository;

    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    public CurrentResponse getCurrent(CurrentRequest currentRequest) {
        if (currentRequest.getName() == null || currentRequest.getCurrency() == null) {
            throw new IllegalArgumentException("Name and currency must not be null");
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String json = response.getBody();

            // Teraz możesz zmapować JSON na obiekt ExchangeRateTable
            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateTable[] exchangeRateTables = objectMapper.readValue(json, ExchangeRateTable[].class);

            // Dostęp do danych
            ExchangeRateTable exchangeRateTable = exchangeRateTables[0];
            List<ExchangeRate> exchangeRates = exchangeRateTable.getRates();
            ExchangeRate exchangeRate = exchangeRates.stream().filter(e -> currentRequest.getCurrency().equals(e.getCode())).findAny().orElse(null);


            if (exchangeRate != null) {
                UserActivity userActivity = new UserActivity(currentRequest.getName(), LocalDate.now(), currentRequest.getCurrency(),exchangeRate.getMid());
                userActivityRepository.save(userActivity);
                return new CurrentResponse(exchangeRate.getMid());
            }
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się pobrać danych walutowych.", e);
        }

        throw new RuntimeException("Nie znaleziono odpowiedniego kursu walutowego.");
    }
    public List<UserActivityDTO> getUsersActivity(){
        return userActivityRepository.findAll().stream().map(e->new UserActivityDTO(e.getName(),e.getRequestDate(),e.getCurrency(),e.getValue())).collect(Collectors.toList());
    }
}
