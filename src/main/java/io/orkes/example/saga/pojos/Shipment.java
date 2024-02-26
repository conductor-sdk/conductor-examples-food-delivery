package io.orkes.example.saga.pojos;

import lombok.Data;

@Data
public class Shipment {
    public enum Status {
        SCHEDULED,
        FAILED,
        DELIVERED,
        CANCELED
    }
    private int id;
    private String orderId;
    private int driverId;
    private String deliveryAddress;
    private String deliveryInstructions;
    private long createdAt;
    private String status;
}
