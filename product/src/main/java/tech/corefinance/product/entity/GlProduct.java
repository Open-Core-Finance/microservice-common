package tech.corefinance.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
@Table(name = "gl_product")
public class GlProduct extends Product {
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> supportedCurrencies;
}
