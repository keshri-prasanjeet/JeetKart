package com.keshri.ecommerce.product;

import com.keshri.ecommerce.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .availableQuantity(product.getAvailableQuantity())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .categoryDescription(product.getCategory().getDescription())
                .build();
    }

    public Product toProduct(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }

        return Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .price(productRequest.price())
                .availableQuantity(productRequest.availableQuantity())
                .description(productRequest.description())
                .category(
                        Category.builder()
                                .id(productRequest.categoryId())
                                .build()
                )
                .build();
    }

    public PurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new PurchaseResponse(
                product.getId(),
                quantity,
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
