package tech.corefinance.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "crypto_product")
public class CryptoProduct extends Product {
    @Column(name = "crypto_address")
    private String cryptoAddress;
    @Column(name = "crypto_private")
    private String cryptoPrivate;
}
