package tech.corefinance.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

@Entity
@Table(name = "exchange_rate")
@Data
public class ExchangeRate implements GenericModel<String>, CreateUpdateDto<String> {
    /**
     * Currency code to sell/buy.
     */
    @Id
    private String id;
    private String name;
    @Column(name = "sell_rate")
    private double sellRate;
    @Column(name = "buy_rate")
    private double buyRate;
}
