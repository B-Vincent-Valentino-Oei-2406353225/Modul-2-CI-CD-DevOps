package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model, RedirectAttributes redirectAttributes) {
        // Validation
        if (product.getProductName() == null || 
            product.getProductName().trim().isEmpty() || 
            product.getProductQuantity() < 0
        ) {
            redirectAttributes.addAttribute("error", "Product name cannot be empty and quantity must be at least 0");
            return "redirect:list";
        }
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable( value="id" ) String id, Model model, RedirectAttributes redirectAttributes) {
        Product product = service.findById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProductPut(@PathVariable( value = "id") String id, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        // Check if product exists
        Product existingProduct = service.findById(id);
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/product/list";
        }
        
        // Validation
        if (product.getProductName() == null || product.getProductName().trim().isEmpty() || product.getProductQuantity() < 0) {
            redirectAttributes.addFlashAttribute("error", "Product name cannot be empty and quantity must be at least 0");
            return "redirect:/product/list";
        }
        
        service.update(id, product);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(@RequestParam(value="error", required=false) String error, Model model) {
        List<Product> allProducts =  service.findAll();
        model.addAttribute("products", allProducts);
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "productList";
    }
}
