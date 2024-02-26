package io.orkes.example.saga.service;

import io.orkes.example.saga.dao.ShipmentDAO;
import io.orkes.example.saga.pojos.Order;
import io.orkes.example.saga.pojos.Shipment;
import io.orkes.example.saga.pojos.ShippingRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

@Slf4j
public class ShipmentService {

    private static final ShipmentDAO shipmentDAO = new ShipmentDAO("jdbc:sqlite:food_delivery.db");

    public static int createShipment(ShippingRequest shippingRequest) {
        int driverId = findDriver();

        Shipment shipment = new Shipment();
        shipment.setOrderId(shippingRequest.getOrderId());
        shipment.setDriverId(driverId);
        shipment.setDeliveryAddress(shippingRequest.getDeliveryAddress());
        shipment.setDeliveryInstructions(shipment.getDeliveryInstructions());

        if (!shipmentDAO.insertShipment(shipment)) {
            log.error("Shipment creation for order {} failed.", shippingRequest.getOrderId());
            return 0;
        }

        log.info("Assigned driver {} to order with id: {}", driverId, shippingRequest.getOrderId());

        return driverId;
    }

    public static void cancelDelivery(String orderId) {
        shipmentDAO.cancelShipment(orderId);
    }

    private static int findDriver() {
        Random random = new Random();
        int driverId = 0;
        int counter = 0;
        while (counter < 5) {
            driverId = random.nextInt(5);
            if(driverId !=0) break;
            counter += 1;
        }
        return driverId;
    }
}
