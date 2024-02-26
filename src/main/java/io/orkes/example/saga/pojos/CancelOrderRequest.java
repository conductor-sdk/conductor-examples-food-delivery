package io.orkes.example.saga.pojos;

import lombok.Data;

@Data
public class CancelOrderRequest {
    private String orderId;
}