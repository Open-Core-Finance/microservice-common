package tech.corefinance.product.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.corefinance.product.converter.StringToExchangeRateId;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateId implements Serializable {
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "to_currency")
    private String toCurrency;

    public ExchangeRateId(String json) {
        var that = new StringToExchangeRateId(new ObjectMapper()).convert(json);
        this.fromCurrency = that.fromCurrency;
        this.toCurrency = that.toCurrency;
    }
}
