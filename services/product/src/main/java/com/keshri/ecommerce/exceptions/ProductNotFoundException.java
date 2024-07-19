package com.keshri.ecommerce.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductNotFoundException extends EntityNotFoundException {
    private final String msg;
}
