package storemanagementtool.store.service;

import org.springframework.stereotype.Service;
import storemanagementtool.store.mapper.ProductMapper;
import storemanagementtool.store.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
}