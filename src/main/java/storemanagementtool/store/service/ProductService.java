package storemanagementtool.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storemanagementtool.store.dto.ProductDto;
import storemanagementtool.store.mapper.ProductMapper;
import storemanagementtool.store.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public ProductDto findProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        List<ProductDto> products = productRepository.findAll().stream()
                .map(productMapper::convertToDto)
                .collect(Collectors.toList());
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products available.");
        }
        return products;
    }
}