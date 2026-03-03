package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private static final String PRODUCT_NOT_FOUND_ERROR = "Product not found";
    private static final String PRODUCT_LIST_REDIRECT = "redirect:/product/list";

    private final ProductService productService;
    private final ProductValidator productValidator;

    public ProductController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        if (!productValidator.isValid(product)) {
            redirectAttributes.addFlashAttribute("error", productValidator.getValidationErrorMessage());
            return "redirect:list";
        }
        productService.create(product);
        return "redirect:list";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", PRODUCT_NOT_FOUND_ERROR);
            return PRODUCT_LIST_REDIRECT;
        }
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProductPut(@PathVariable("id") String id, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("error", PRODUCT_NOT_FOUND_ERROR);
            return PRODUCT_LIST_REDIRECT;
        }

        if (!productValidator.isValid(product)) {
            redirectAttributes.addFlashAttribute("error", productValidator.getValidationErrorMessage());
            return PRODUCT_LIST_REDIRECT;
        }

        productService.update(id, product);
        return PRODUCT_LIST_REDIRECT;
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable( value = "id", required=false) String id, RedirectAttributes redirectAttributes) {
        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("error", PRODUCT_NOT_FOUND_ERROR);
            return PRODUCT_LIST_REDIRECT;
        }

        productService.deleteById(id);
        return PRODUCT_LIST_REDIRECT;
    }

    @GetMapping("/list")
    public String productListPage(@RequestParam(value="error", required=false) String error, Model model) {
        List<Product> allProducts =  productService.findAll();
        model.addAttribute("products", allProducts);
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "productList";
    }
}
