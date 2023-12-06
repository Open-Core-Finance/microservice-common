package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table(name = "organization")
@Data
public class Organization implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Column(name = "street_address_line_1")
    private String streetAddressLine1;
    private String city;
    private String state;
    @Column(name = "zip_postal_code")
    private String zipPostalCode;
    private String country;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    private String timezone;
    @Column(name = "local_date_format")
    private String localDateFormat;
    @Column(name = "local_date_time_format")
    private String localDateTimeFormat;
    @Column(name = "decimal_mark")
    private String decimalMark;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "non_working_days")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<DayOfWeek> nonWorkingDays = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
}
