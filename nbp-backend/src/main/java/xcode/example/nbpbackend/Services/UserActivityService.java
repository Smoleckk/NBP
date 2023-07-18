package xcode.example.nbpbackend.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
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
    private final RestTemplate restTemplate;

    @Value("${nbp.url}")
    private String nbpUrl;
    public UserActivityService(UserActivityRepository userActivityRepository, RestTemplate restTemplate) {
        this.userActivityRepository = userActivityRepository;
        this.restTemplate = restTemplate;
    }


    public CurrentResponse getCurrent(CurrentRequest currentRequest) {
        if (currentRequest.getName() == null || currentRequest.getCurrency() == null) {
            throw new IllegalArgumentException("Name and currency must not be null");
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(nbpUrl, String.class);
            String json = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateTable[] exchangeRateTables = objectMapper.readValue(json, ExchangeRateTable[].class);

            ExchangeRateTable exchangeRateTable = exchangeRateTables[0];
            List<ExchangeRate> exchangeRates = exchangeRateTable.getRates();
            ExchangeRate exchangeRate = exchangeRates.stream().filter(e -> currentRequest.getCurrency().equals(e.getCode())).findAny().orElse(null);


            if (exchangeRate != null) {
                UserActivity userActivity = new UserActivity(currentRequest.getName(), LocalDate.now(), currentRequest.getCurrency(),exchangeRate.getMid());
                userActivityRepository.save(userActivity);
                return new CurrentResponse(exchangeRate.getMid());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve currency data.", e);
        }

        throw new RuntimeException("No appropriate exchange rate found.");
    }
    public List<UserActivityDTO> getUsersActivity(){
        return userActivityRepository.findAll().stream().map(e->new UserActivityDTO(e.getName(),e.getRequestDate(),e.getCurrency(),e.getValue())).collect(Collectors.toList());
    }
}
