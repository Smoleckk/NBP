package xcode.example.nbpbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<ExchangeRate> rates;

    public ExchangeRateTable(String table, String no, String effectiveDate) {
        this.table = table;
        this.no = no;
        this.effectiveDate = effectiveDate;
    }
}
