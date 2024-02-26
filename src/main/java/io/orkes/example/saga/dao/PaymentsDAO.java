package io.orkes.example.saga.dao;

import io.orkes.example.saga.pojos.Order;
import io.orkes.example.saga.pojos.Payment;
import io.orkes.example.saga.pojos.PaymentMethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PaymentsDAO extends BaseDAO {

    public PaymentsDAO(String url) {
        super(url);
    }

    public String insertPayment(Payment payment) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        String sql = "INSERT INTO payments(paymentId, orderId, amount, method, createdAt, status) VALUES(?,?,?,?,?,?);";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2, payment.getOrderId());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentMethod().toString());
            pstmt.setString(5, nowAsISO);
            pstmt.setString(6, payment.getStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "";
    }

    public String updatePayment(Payment payment) {
        String sql = "UPDATE payments SET amount=?, method=?, status=? WHERE paymentId=?;";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, payment.getAmount());
            pstmt.setString(2, payment.getPaymentMethod().toString());
            pstmt.setString(3, payment.getStatus().name());
            pstmt.setString(4, payment.getPaymentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "";
    }

    public void readPayment(String orderId, Payment payment) {
        String sql = "SELECT paymentId, orderId, amount, method, createdAt, status FROM payments WHERE orderId = ?";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                payment.setPaymentId(rs.getString("paymentId"));
                payment.setOrderId(rs.getString("orderId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setCreatedAt(rs.getLong("createdAt"));
                payment.setStatus(Payment.Status.valueOf(rs.getString("status")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
