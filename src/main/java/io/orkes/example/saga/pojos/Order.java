package io.orkes.example.saga.pojos;

import lombok.Data;

import java.util.EnumMap;

@Data
public class Order {

    public enum Status {
        PENDING,
        ASSIGNED,
        CONFIRMED,
        CANCELLED
    }

    private String orderId;
    private int customerId;
    private int restaurantId;
    private String deliveryAddress;
    private long createdAt;
    private Status status;
    private OrderDetails orderDetails;
}
