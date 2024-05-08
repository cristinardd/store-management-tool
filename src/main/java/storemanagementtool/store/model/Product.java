package storemanagementtool.store.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity(name = "product")
@Builder
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;
}
