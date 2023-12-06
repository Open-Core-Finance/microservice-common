package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

@Data
@Entity
@Table(name = "branch")
public class Branch implements GenericModel<String>, CreateUpdateDto<String> {
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
    @Column(name = "parent_branch_id")
    private String parentBranchId;
}
