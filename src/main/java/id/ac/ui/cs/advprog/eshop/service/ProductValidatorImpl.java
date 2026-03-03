package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductValidatorImpl implements ProductValidator {
    private static final String VALIDATION_ERROR_MESSAGE =
            "Product name cannot be empty and quantity must be at least 0";

    @Override
    public boolean isValid(Product product) {
        return product != null
                && product.getProductName() != null
                && !product.getProductName().trim().isEmpty()
                && product.getProductQuantity() >= 0;
    }

    @Override
    public String getValidationErrorMessage() {
        return VALIDATION_ERROR_MESSAGE;
    }
}
