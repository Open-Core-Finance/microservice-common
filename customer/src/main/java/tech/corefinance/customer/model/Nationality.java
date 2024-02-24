package tech.corefinance.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import tech.corefinance.customer.enums.CustomerIdentityType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Nationality implements Serializable {
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "identity_type")
    @Enumerated(EnumType.STRING)
    private CustomerIdentityType identityType;
    @Column(name = "identity_number")
    private String identityNumber;
    @Column(name = "issuing_country")
    private String issuingCountry;
    @Column(name = "issuing_place")
    private String issuingPlace;
    @Column(name = "issuing_date")
    private LocalDate issuingDate;
    @Column(name = "expiring_date")
    private LocalDate expiringDate;
}
