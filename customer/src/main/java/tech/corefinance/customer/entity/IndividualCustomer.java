package tech.corefinance.customer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.enums.Gender;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.customer.enums.CustomerIdentityType;
import tech.corefinance.customer.enums.MaritalStatus;
import tech.corefinance.customer.model.Nationality;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "individual_customer")
@EqualsAndHashCode(callSuper = true)
public class IndividualCustomer extends Customer implements CreateUpdateDto<Long> {

    @Column(name = "contact_home_phone")
    private String contactHomePhone;

    private String title;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "cis_number")
    private String cisNumber;

    @Column(name = "place_of_birth")
    private String placeOfBirth;
    private LocalDate dob;

    @JdbcTypeCode(SqlTypes.JSON)
    private Nationality nationality;
    @Column(name = "single_nationality")
    private boolean singleNationality = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "second_nationality")
    private Nationality secondNationality;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
}
