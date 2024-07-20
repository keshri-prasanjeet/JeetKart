package com.keshri.ecommerce.product;

import com.keshri.ecommerce.exceptions.ProductNotFoundException;
import com.keshri.ecommerce.exceptions.ProductPurchaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public Integer createProduct(ProductRequest productRequest) {

        var product = productRepository.save(productMapper.toProduct(productRequest));
        return product.getId();
    }

    public List<PurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequestList) {
        var productIds = productPurchaseRequestList.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

//        productIds.forEach(productId -> {
//            if(!existsById(productId)){
//                throw new ProductNotFoundException(
//                        format("Product with id %d not found", productId)
//                );
//            }
//        });
        var storedProducts = productRepository.findAllByIdInOrderById(productIds); //sorted the inventory result
        if(productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        var sortedRequest = productPurchaseRequestList.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();//sorted the purchase request

        List<PurchaseResponse> purchasedProducts = new ArrayList<>();

        for(int i = 0; i < storedProducts.size(); i++) {
            var productInInventory = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);
            if(productInInventory.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock for product with ID::" + productRequest.productId());
            }
            var newAvailableQuantity = productInInventory.getAvailableQuantity() - productRequest.quantity();
            productInInventory.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(productInInventory);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(productInInventory, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public Boolean existsById(Integer productId) {
        return productRepository.existsById(productId);
    }

    public ProductResponse findProductById(Integer productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        format("No product found with the product ID: %d", productId)
                ));

        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
