package storemanagementtool.store.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    @NotNull(message = "Product name is required.")
    @Size(min = 1, message = "Product name must not be empty.")
    private String name;

    @NotNull(message = "Product price is required.")
    @Positive(message = "Product price must be positive.")
    private Double price;

    @NotNull(message = "Product quantity is required.")
    @Positive(message = "Product quantity must be greater than zero.")
    private Integer quantity;
}