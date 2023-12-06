package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

@Data
@Table(name = "currency")
@Entity
public class Currency implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String symbol;
    @Column(name = "decimal_mark")
    private String decimalMark;
    @Column(name = "symbol_at_beginning")
    private boolean symbolAtBeginning;
}
