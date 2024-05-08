package storemanagementtool.store.mapper;

import org.springframework.stereotype.Component;
import storemanagementtool.store.dto.ProductDto;
import storemanagementtool.store.model.Product;

@Component
public class ProductMapper {
    public ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public Product convertToEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();
    }
}
