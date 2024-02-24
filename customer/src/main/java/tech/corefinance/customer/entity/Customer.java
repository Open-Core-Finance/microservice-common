package tech.corefinance.customer.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.time.ZonedDateTime;

@MappedSuperclass
@Data
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public abstract class Customer implements GenericModel<Long>, AuditableEntity<BasicUserDto>, ModifiedDateTrackedEntity<ZonedDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @CreatedBy
    @Column(name = "created_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto createdBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;
    @CreatedBy
    @Column(name = "last_modified_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto lastModifiedBy;

    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_company_phone")
    private String contactCompanyPhone;

    @Column(name = "mailing_street_address_line_1")
    private String mailingStreetAddressLine1;
    @Column(name = "mailing_street_address_line_2")
    private String mailingStreetAddressLine2;
    @Column(name = "mailing_district")
    private String mailingDistrict;
    @Column(name = "mailing_city")
    private String mailingCity;
    @Column(name = "mailing_state")
    private String mailingState;
    @Column(name = "mailing_zip_postal_code")
    private String mailingZipPostalCode;
    @Column(name = "malling_country")
    private String mailingCountry;

    @Column(name = "street_address_line_1")
    private String streetAddressLine1;
    @Column(name = "street_address_line_2")
    private String streetAddressLine2;
    @Column(name = "district")
    private String district;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip_postal_code")
    private String zipPostalCode;
    @Column(name = "country")
    private String country;

    @Column(name = "consent_marketing")
    private boolean consentMarketing;
    @Column(name = "consent_non_marketing")
    private boolean consentNonMarketing;
    @Column(name = "consent_abroad")
    private boolean consentAbroad;
    @Column(name = "consent_transfer_to_3rd")
    private boolean consentTransferToThirdParty;
}
