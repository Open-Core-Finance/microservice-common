package tech.corefinance.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.common.model.CreateUpdateDto;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "corporate_customer")
@EqualsAndHashCode(callSuper = true)
public class CorporateCustomer extends Customer implements CreateUpdateDto<Long> {
    private String name;
    @Column(name = "tax_number")
    private String taxNumber;
    @Column(name = "start_date")
    private LocalDate startDate;
}
