package io.orkes.example.saga.pojos;

import lombok.Data;

@Data
public class CancelPaymentRequest {
    private String orderId;
}
