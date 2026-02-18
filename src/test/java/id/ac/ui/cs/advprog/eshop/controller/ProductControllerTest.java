package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void createProductPageReturnsViewAndModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPostWithValidInputCallsServiceAndRedirects() throws Exception {
        Product created = new Product();
        created.setProductName("Laptop");
        created.setProductQuantity(10);
        when(productService.create(any(Product.class))).thenReturn(created);

        mockMvc.perform(post("/product/create")
                        .param("productName", "Laptop")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService).create(any(Product.class));
    }

    @Test
    void createProductPostWithInvalidInputRedirectsWithError() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", " ")
                        .param("productQuantity", "-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void createProductPostWithNullNameRedirectsWithError() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void createProductPostWithNegativeQuantityOnlyRedirectsWithError() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Valid Name")
                        .param("productQuantity", "-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void editProductPageWhenFoundReturnsEditView() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-1");
        existing.setProductName("Mouse");
        existing.setProductQuantity(5);
        when(productService.findById("p-1")).thenReturn(existing);

        mockMvc.perform(get("/product/edit/p-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", existing));
    }

    @Test
    void editProductPageWhenNotFoundRedirectsWithError() throws Exception {
        when(productService.findById("missing")).thenReturn(null);

        mockMvc.perform(get("/product/edit/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product not found"));
    }

    @Test
    void editProductPutWhenNotFoundRedirectsWithError() throws Exception {
        when(productService.findById("missing")).thenReturn(null);

        mockMvc.perform(post("/product/edit/missing")
                        .param("productName", "Keyboard")
                        .param("productQuantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product not found"));

        verify(productService, never()).update(eq("missing"), any(Product.class));
    }

    @Test
    void editProductPutWithInvalidInputRedirectsWithError() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-2");
        when(productService.findById("p-2")).thenReturn(existing);

        mockMvc.perform(post("/product/edit/p-2")
                        .param("productName", "")
                        .param("productQuantity", "-2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).update(eq("p-2"), any(Product.class));
    }

    @Test
    void editProductPutWithNullNameRedirectsWithError() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-2a");
        when(productService.findById("p-2a")).thenReturn(existing);

        mockMvc.perform(post("/product/edit/p-2a")
                        .param("productQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).update(eq("p-2a"), any(Product.class));
    }

    @Test
    void editProductPutWithNegativeQuantityOnlyRedirectsWithError() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-2b");
        when(productService.findById("p-2b")).thenReturn(existing);

        mockMvc.perform(post("/product/edit/p-2b")
                        .param("productName", "Valid Name")
                        .param("productQuantity", "-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product name cannot be empty and quantity must be at least 0"));

        verify(productService, never()).update(eq("p-2b"), any(Product.class));
    }

    @Test
    void editProductPutWithValidInputUpdatesAndRedirects() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-3");
        when(productService.findById("p-3")).thenReturn(existing);

        mockMvc.perform(post("/product/edit/p-3")
                        .param("productName", "Monitor")
                        .param("productQuantity", "8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).update(eq("p-3"), any(Product.class));
    }

    @Test
    void deleteProductWhenFoundDeletesAndRedirects() throws Exception {
        Product existing = new Product();
        existing.setProductId("p-4");
        when(productService.findById("p-4")).thenReturn(existing);

        mockMvc.perform(get("/product/delete/p-4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).deleteById("p-4");
    }

    @Test
    void deleteProductWhenMissingRedirectsWithError() throws Exception {
        when(productService.findById("missing")).thenReturn(null);

        mockMvc.perform(get("/product/delete/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"))
                .andExpect(flash().attribute("error", "Product not found"));

        verify(productService, never()).deleteById("missing");
    }

    @Test
    void productListPageReturnsProducts() throws Exception {
        Product product = new Product();
        product.setProductId("p-5");
        product.setProductName("Speaker");
        product.setProductQuantity(2);
        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void productListPageWithErrorParamAddsErrorToModel() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/product/list").param("error", "Product not found"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("error", "Product not found"));
    }
}