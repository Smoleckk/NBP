package xcode.example.nbpbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentRequest {

    private String currency;
    private String name;
}
