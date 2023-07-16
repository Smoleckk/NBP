package xcode.example.nbpbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserActivityDTO {
    private String name;
    private LocalDate requestDate;
    private String currency;
    private Double value;
}
