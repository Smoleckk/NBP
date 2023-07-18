package xcode.example.nbpbackend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import xcode.example.nbpbackend.Repositories.UserActivityRepository;
import xcode.example.nbpbackend.Services.UserActivityService;
import xcode.example.nbpbackend.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserActivityServiceTest {

    @Mock
    private UserActivityRepository userActivityRepository;
    private UserActivityService userActivityService;
    @Mock
    private RestTemplate restTemplate;

    private String json;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userActivityService = new UserActivityService(userActivityRepository,restTemplate);
        ReflectionTestUtils.setField(userActivityService, "nbpUrl", "https://example.com");
         json = "[{\"table\":\"A\",\"no\":\"136/A/NBP/2023\",\"effectiveDate\":\"2023-07-17\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1144},{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":3.9616}]}]";

    }

    @Test
    void getCurrent_ValidRequest_ReturnsCurrentResponse(){
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest("USD", "Tom Cruse");
        String json = "[{\"table\":\"A\",\"no\":\"136/A/NBP/2023\",\"effectiveDate\":\"2023-07-17\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1144},{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":3.9616}]}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(json, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(null);

        // Act
        CurrentResponse currentResponse = userActivityService.getCurrent(currentRequest);

        // Assert
        assertNotNull(currentResponse);
        assertEquals(3.9616, currentResponse.getValue());
    }

    @Test
    void getCurrent_IncorrectCurrencyCode_ThrowsRuntimeException() {
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest("WroongCurreny", "Tom Cruse");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(json, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userActivityService.getCurrent(currentRequest));
    }

    @Test
    void getCurrent_NullCurrencyCode_ThrowsIllegalArgumentException() {
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest(null, "Tom Cruse");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(json, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userActivityService.getCurrent(currentRequest));
    }

    @Test
    void getCurrent_NoExchangeRateFound_ThrowsRuntimeException() {
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest("USD", "Tom Cruse");
        String jsonWithoutRates = "[{\"table\":\"A\",\"no\":\"136/A/NBP/2023\",\"effectiveDate\":\"2023-07-17\",\"rates\":[]}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonWithoutRates, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userActivityService.getCurrent(currentRequest));
    }

    @Test
    void getCurrent_ExceptionThrownByRestTemplate_ThrowsRuntimeException() {
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest("USD", "Tom Cruse");

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(new RuntimeException("Failed to retrieve currency data."));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userActivityService.getCurrent(currentRequest));
    }

    @Test
    void getCurrent_SaveUserActivity_WhenExchangeRateFound() {
        // Arrange
        CurrentRequest currentRequest = new CurrentRequest("USD", "Tom Cruse");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(json, HttpStatus.OK);
        UserActivity expectedUserActivity = new UserActivity("Tom Cruse", LocalDate.now(), "USD", 3.9616);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(null);

        // Act
        userActivityService.getCurrent(currentRequest);

        // Assert
        verify(userActivityRepository, times(1)).save(expectedUserActivity);
    }

    @Test
    void getUsersActivity_ReturnsUserActivityDTOList() {
        // Arrange
        List<UserActivity> userActivities = new ArrayList<>();
        userActivities.add(new UserActivity("John", LocalDate.now(), "USD", 3.7937));
        when(userActivityRepository.findAll()).thenReturn(userActivities);

        // Act
        List<UserActivityDTO> userActivityDTOs = userActivityService.getUsersActivity();

        // Assert
        assertNotNull(userActivityDTOs);
        assertEquals(1, userActivityDTOs.size());
        UserActivityDTO userActivityDTO = userActivityDTOs.get(0);
        assertEquals("John", userActivityDTO.getName());
        assertEquals(LocalDate.now(), userActivityDTO.getRequestDate());
        assertEquals("USD", userActivityDTO.getCurrency());
        assertEquals(3.7937, userActivityDTO.getValue());
    }
    @Test
    void getUsersActivity_NoUserActivities_ReturnsEmptyList() {
        // Arrange
        when(userActivityRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<UserActivityDTO> userActivityDTOs = userActivityService.getUsersActivity();

        // Assert
        assertNotNull(userActivityDTOs);
        assertTrue(userActivityDTOs.isEmpty());
    }
}
