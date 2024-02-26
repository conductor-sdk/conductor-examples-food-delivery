package io.orkes.example.saga.dao;

import io.orkes.example.saga.pojos.Shipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ShipmentDAO extends BaseDAO {

    public ShipmentDAO(String url) {
        super(url);
    }

    public boolean insertShipment(Shipment shipment) {
        Date date = new Date();
        Timestamp nowAsTS = new Timestamp(date.getTime());

        String sql = "INSERT INTO shipments(orderId,driverId,address,instructions,createdAt,status) VALUES(?,?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shipment.getOrderId());
            pstmt.setInt(2, shipment.getDriverId());
            pstmt.setString(3, shipment.getDeliveryAddress());
            pstmt.setString(4, shipment.getDeliveryInstructions());
            pstmt.setTimestamp(5, nowAsTS);
            pstmt.setString(6, Shipment.Status.SCHEDULED.name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean cancelShipment(String orderId) {
        String sql = "UPDATE shipments SET status=? WHERE orderId=?;";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Shipment.Status.CANCELED.name());
            pstmt.setString(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
