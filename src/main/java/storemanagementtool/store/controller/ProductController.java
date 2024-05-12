package storemanagementtool.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import storemanagementtool.store.dto.ProductDto;
import storemanagementtool.store.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProductPrice(@PathVariable Long id, @RequestParam double price) {
        return ResponseEntity.ok(productService.updateProductPrice(id, price));
    }

    @PatchMapping("/{id}/buy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> buyProduct(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.buyProduct(id, quantity));
    }
}