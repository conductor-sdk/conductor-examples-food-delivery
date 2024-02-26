package io.orkes.example.saga.pojos;

import lombok.Data;

@Data
public class PaymentMethod {
    private String type;
    private PaymentDetails details;
}
