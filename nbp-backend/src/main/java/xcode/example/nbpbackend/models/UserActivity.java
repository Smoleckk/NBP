package xcode.example.nbpbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDate requestDate;
    private String currency;
    private Double value;

    public UserActivity(String name, LocalDate requestDate, String currency, Double value) {
        this.name = name;
        this.requestDate = requestDate;
        this.currency = currency;
        this.value = value;
    }
}
