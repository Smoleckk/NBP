package xcode.example.nbpbackend.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xcode.example.nbpbackend.Services.UserActivityService;
import xcode.example.nbpbackend.models.CurrentRequest;
import xcode.example.nbpbackend.models.CurrentResponse;
import xcode.example.nbpbackend.models.UserActivity;
import xcode.example.nbpbackend.models.UserActivityDTO;

import java.util.List;

@RestController
@RequestMapping(path = "currencies")
public class UserActivityController {

    private final UserActivityService userService;

    public UserActivityController(UserActivityService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/get-current-currency-value-command")
    public ResponseEntity<CurrentResponse> getCurrent(@RequestBody CurrentRequest currentRequest) {
        try {
            CurrentResponse response = userService.getCurrent(currentRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping(path = "/requests")
    public ResponseEntity<List<UserActivityDTO>> getUsersActivity() {
        return ResponseEntity.ok(userService.getUsersActivity());
    }
}
