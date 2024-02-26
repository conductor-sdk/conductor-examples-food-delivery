package io.orkes.example.saga.pojos;

import lombok.Data;

@Data
public class Shipment {
    private int id;
    private String orderId;
    private String deliveryAddress;
    private String deliveryInstructions;
    private long createdAt;
    private String status;
}
