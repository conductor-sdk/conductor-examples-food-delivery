package io.orkes.example.saga.service;

import io.orkes.example.saga.dao.OrdersDAO;
import io.orkes.example.saga.pojos.Order;
import io.orkes.example.saga.pojos.OrderDetails;
import io.orkes.example.saga.pojos.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {

    private static final OrdersDAO ORDERS_DAO = new OrdersDAO("jdbc:sqlite:food_delivery.db");

    public static String createOrder(OrderRequest orderRequest) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        Order order = new Order();
        order.setOrderId(uuidAsString);
        order.setCustomerId(orderRequest.getCustomerId());
        order.setRestaurantId(orderRequest.getRestaurantId());
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        order.setStatus(Order.Status.PENDING);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(uuidAsString);
        orderDetails.setItems(orderRequest.getItems());
        orderDetails.setNotes(orderRequest.getNotes());

        order.setOrderDetails(orderDetails);

        String error = ORDERS_DAO.insertOrder(order);

        if (error.isEmpty()) {
            log.info("Created order with id: {}", order.getOrderId());
        }
        else {
            log.error("Order creation failure: {}", error);
            return null;
        }

        return uuidAsString;
    }

    public static Order getOrder(String orderId) {
        Order order = new Order();
        ORDERS_DAO.readOrder(orderId, order);
        return order;
    }

//    public static boolean assignDriverToOrder(Order order, int driverId) {
//        order.setDriverId(driverId);
//        order.setStatus(Order.Status.ASSIGNED);
//        return ORDERS_DAO.updateOrder(order);
//    }

//    public static boolean confirmOrder(Order order) {
//        order.setStatus(Order.Status.CONFIRMED);
//        log.info("Confirming order {}", order.getOrderId());
//        return ORDERS_DAO.updateOrder(order);
//    }
//
    public static void cancelOrder(Order order) {
        order.setStatus(Order.Status.CANCELLED);
        log.info("Cancelling order {}", order.getOrderId());
        ORDERS_DAO.updateOrder(order);
    }
}
