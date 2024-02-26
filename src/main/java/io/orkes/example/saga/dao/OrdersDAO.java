package io.orkes.example.saga.dao;

import io.orkes.example.saga.pojos.Order;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;

public class OrdersDAO extends BaseDAO {

    public OrdersDAO(String url) {
        super(url);
    }

    public String insertOrder(Order order) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        Date date = new Date();
        Timestamp nowAsTS = new Timestamp(date.getTime());

        String itemsStr = String.join("", order.getOrderDetails().getItems().toString());

        String notesStr = null;

        if(!order.getOrderDetails().getNotes().isEmpty()) {
            notesStr = String.join("", order.getOrderDetails().getNotes().toString());
        } else {
            notesStr = "";
        }

        String sql = "INSERT INTO orders(orderId,customerId,restaurantId,deliveryAddress,createdAt,status) VALUES(?,?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getOrderId());
            pstmt.setInt(2, order.getCustomerId());
            pstmt.setInt(3, order.getRestaurantId());
            pstmt.setString(4, order.getDeliveryAddress());
            pstmt.setTimestamp(5, nowAsTS);
            pstmt.setString(6, order.getStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        sql = "INSERT INTO orders_details(orderId,items,notes) VALUES(?,?,?)";



        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, itemsStr);
            pstmt.setString(3, notesStr);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "";
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET customerId=?,restaurantId=?,deliveryAddress=?,status=? WHERE orderId=?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getRestaurantId());
            pstmt.setString(3, order.getDeliveryAddress());
            pstmt.setString(4, order.getStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void readOrder(String orderId, Order order) {
        String sql = "SELECT orderId, customerId, restaurantId, deliveryAddress, createdAt, status  FROM orders WHERE orderId = ?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                order.setOrderId(rs.getString("orderId"));
                order.setCustomerId(rs.getInt("customerId"));
                order.setRestaurantId(rs.getInt("restaurantId"));
                order.setDeliveryAddress(rs.getString("deliveryAddress"));
                order.setCreatedAt(rs.getLong("createdAt"));
                order.setStatus(Order.Status.valueOf(rs.getString("status")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
