package storemanagementtool.store.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import storemanagementtool.store.dto.ProductDto;
import storemanagementtool.store.exception.custom.ProductOutOfStockException;
import storemanagementtool.store.mapper.ProductMapper;
import storemanagementtool.store.model.Product;
import storemanagementtool.store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;

    @Test
    void givenProductExists_whenFindProductById_thenProductFound() {
        Product product = buildProduct(50.0, 10);
        ProductDto productDto = buildProductDto(50.0, 10);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductDto result = productService.findProductById(1L);

        assertNotNull(result);
        assertEquals(productDto, result);

        verify(productRepository).findById(anyLong());
        verify(productMapper).convertToDto(product);

        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void givenProductDoesNotExist_whenFindProductById_thenThrowsNoSuchElementException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.findProductById(1L));
    }

    @Test
    void givenProductsExist_whenGetAllProducts_thenProductsRetrieved() {
        Product product = buildProduct(50.0, 10);
        ProductDto productDto = buildProductDto(50.0, 10);
        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(productDto);

        List<ProductDto> result = productService.getAllProducts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(productDtos, result);

        verify(productRepository).findAll();
        verify(productMapper, times(products.size())).convertToDto(any(Product.class));

        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void givenNoProductsExist_whenGetAllProducts_thenThrowsNoSuchElementException() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NoSuchElementException.class, () -> productService.getAllProducts());
    }

    @Test
    void givenValidProduct_whenAddProduct_thenProductAdded() {
        Product product = buildProduct(50.0, 10);
        ProductDto productDto = buildProductDto(50.0, 10);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.convertToEntity(any(ProductDto.class))).thenReturn(product);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.addProduct(productDto);

        assertNotNull(result);
        assertEquals(productDto, result);

        verify(productRepository).save(any(Product.class));
        verify(productMapper).convertToEntity(productDto);
        verify(productMapper).convertToDto(product);

        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void givenProductExists_whenUpdateProductPrice_thenPriceUpdated() {
        Product product = buildProduct(50.0, 10);
        Product updatedProduct = buildProduct(55.0, 10);
        ProductDto updatedDto = buildProductDto(55.0, 10);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(updatedDto);

        ProductDto result = productService.updateProductPrice(1L, 55.0);

        assertNotNull(result);
        assertEquals(55.0, result.getPrice());

        verify(productRepository).findById(anyLong());
        verify(productRepository).save(product);
        verify(productMapper).convertToDto(updatedProduct);

        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void givenNonexistentProduct_whenUpdateProductPrice_thenThrowsNoSuchElementException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.updateProductPrice(1L, 55.0));
    }

    @Test
    void givenProductInStock_whenBuyProduct_thenQuantityDecreases() {
        Product product = buildProduct(50.0, 10);
        Product updatedProduct = buildProduct(50.0, 10 - 1);
        ProductDto updatedDto = buildProductDto(55.0, 10 -1);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(updatedDto);

        ProductDto result = productService.buyProduct(1L, 1);

        assertNotNull(result);
        assertEquals(9, result.getQuantity());

        verify(productRepository).findById(anyLong());
        verify(productRepository).save(product);

        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void givenProductExistsButOutOfStock_whenBuyProduct_thenThrowsOutOfStockException() {
        Product product = buildProduct(50.0, 1);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThrows(ProductOutOfStockException.class, () -> productService.buyProduct(1L, 2));
    }

    private Product buildProduct(Double price, Integer quantity) {
        return Product.builder()
                .id(1L)
                .name("Sample Product")
                .price(price)
                .quantity(quantity)
                .build();
    }

    private ProductDto buildProductDto(Double price, Integer quantity) {
        return ProductDto.builder()
                .id(1L)
                .name("Sample Product")
                .price(price)
                .quantity(quantity)
                .build();
    }
}
