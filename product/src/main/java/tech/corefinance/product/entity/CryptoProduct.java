package tech.corefinance.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "crypto_product")
@EqualsAndHashCode(callSuper = true)
public class CryptoProduct extends Product {
}
