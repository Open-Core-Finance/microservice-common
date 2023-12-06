package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "holiday")
public class Holiday implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "holiday_date")
    private LocalDate holidayDate;
    @Column(name = "repeat_yearly")
    private boolean repeatYearly;
    private String description;
}
