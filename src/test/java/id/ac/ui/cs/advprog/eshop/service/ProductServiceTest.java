package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.create(product)).thenReturn(product);
        
        Product result = productService.create(product);
        
        assertEquals(product.getProductId(), result.getProductId());
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getProductQuantity(), result.getProductQuantity());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindById_Success() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(product);
        
        Product result = productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        
        assertNotNull(result);
        assertEquals(product.getProductId(), result.getProductId());
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById("non-existent-id")).thenReturn(null);
        
        Product result = productService.findById("non-existent-id");
        
        assertNull(result);
    }

    @Test
    void testFindAll() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-aabf-d0821dda909a");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList.iterator());
        
        List<Product> result = productService.findAll();
        
        assertEquals(2, result.size());
        assertEquals(product1.getProductId(), result.get(0).getProductId());
        assertEquals(product2.getProductId(), result.get(1).getProductId());
    }

    @Test
    void testEditProduct_Success() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang - Updated");
        updatedProduct.setProductQuantity(150);

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        
        Product result = productService.update(productId, updatedProduct);
        
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Sampo Cap Bambang - Updated", result.getProductName());
        assertEquals(150, result.getProductQuantity());
    }

    @Test
    void testEditProduct_UpdateName() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang Premium");
        updatedProduct.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        
        Product result = productService.update(productId, updatedProduct);
        
        assertNotNull(result);
        assertEquals("Sampo Cap Bambang Premium", result.getProductName());
        assertEquals(100, result.getProductQuantity());
    }

    @Test
    void testEditProduct_UpdateQuantity() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang");
        updatedProduct.setProductQuantity(200);

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        
        Product result = productService.update(productId, updatedProduct);
        
        assertNotNull(result);
        assertEquals("Sampo Cap Bambang", result.getProductName());
        assertEquals(200, result.getProductQuantity());
    }

    @Test
    void testEditProduct_ProductNotFound() {
        String nonExistentId = "non-existent-id";
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang - Updated");
        updatedProduct.setProductQuantity(150);

        when(productRepository.findById(nonExistentId)).thenReturn(null);
        
        Product result = productService.update(nonExistentId, updatedProduct);
        
        assertNull(result);
    }

    @Test
    void testEditProduct_UpdateToEmptyName() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("");
        updatedProduct.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        
        Product result = productService.update(productId, updatedProduct);
        
        assertNotNull(result);
        assertEquals("", result.getProductName());
    }

    @Test
    void testEditProduct_UpdateToZeroQuantity() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang");
        updatedProduct.setProductQuantity(0);

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        
        Product result = productService.update(productId, updatedProduct);
        
        assertNotNull(result);
        assertEquals(0, result.getProductQuantity());
    }

    @Test
    void testDeleteProduct_Success() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        
        productService.deleteById(productId);
        
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_NotFound() {
        String nonExistentId = "non-existent-id";
        
        productService.deleteById(nonExistentId);
        
        verify(productRepository, times(1)).deleteById(nonExistentId);
    }
}
