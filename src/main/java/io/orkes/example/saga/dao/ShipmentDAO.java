package io.orkes.example.saga.dao;

import io.orkes.example.saga.pojos.Shipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ShipmentDAO extends BaseDAO {

    public ShipmentDAO(String url) {
        super(url);
    }

//    public boolean insertAssignment(Shipment shipment) {
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//
//        String sql = "INSERT INTO assignments(order_id,driver_id,created_at,active) VALUES(?,?,?,?)";
//
//        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, shipment.getOrderId());
//            pstmt.setInt(2, shipment.getDriverId());
//            pstmt.setString(3, nowAsISO);
//            pstmt.setBoolean(4, shipment.getActive());
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return false;
//        }
//        return true;
//    }
//
//    public boolean deactivateAssignment(String orderId) {
//        String sql = "UPDATE assignments SET active=? WHERE order_id=?;";
//
//        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setBoolean(1, false);
//            pstmt.setString(2, orderId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return false;
//        }
//        return true;
//    }
}
