package com.keshri.ecommerce.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CustomerNotFoundException extends RuntimeException {
        private final String msg;
}
