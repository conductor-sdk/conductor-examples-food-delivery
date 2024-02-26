package io.orkes.example.saga.service;

import io.orkes.example.saga.dao.ShipmentDAO;
import io.orkes.example.saga.pojos.Order;
import io.orkes.example.saga.pojos.Shipment;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
@Slf4j
public class ShipmentService {

    private static final ShipmentDAO shipmentDAO = new ShipmentDAO("jdbc:sqlite:food_delivery.db");

//    public static int assignDriver(String orderId) {
//        int driverId = findDriver();
//
//        Order order = OrderService.getOrder(orderId);
//
//        if (order.getOrderId().isEmpty()) {
//            log.error("Order with id {} not found.", orderId);
//            return 0;
//        }
//
//        Shipment shipment = new Shipment();
//        shipment.setOrderId(orderId);
//        shipment.setActive(true);
//
//        if (!shipmentDAO.insertAssignment(shipment)) {
//            log.error("Cab assignment for order {} failed.", orderId);
//            return 0;
//        }
//
//        OrderService.assignDriverToOrder(order, driverId);
//
//        log.info("Assigned driver {} to order with id: {}", driverId, orderId);
//
//        return driverId;
//    }
//
//    public static void cancelAssignment(String orderId) {
//        Order order = OrderService.getOrder(orderId);
//
//        if (order.getOrderId().isEmpty()) {
//            log.error("Order with id {} not found.", orderId);
//        }
//
//        shipmentDAO.deactivateAssignment(orderId);
//    }
//
//    private static int findDriver() {
//        Random random = new Random();
//        int driverId = 0;
//        while (true) {
//            driverId = random.nextInt(5);
//            if(driverId !=0) break;
//        }
//        return driverId;
//    }
}
