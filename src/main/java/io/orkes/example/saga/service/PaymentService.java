package io.orkes.example.saga.service;

import io.orkes.example.saga.dao.PaymentsDAO;
import io.orkes.example.saga.pojos.Payment;
import io.orkes.example.saga.pojos.PaymentMethod;
import io.orkes.example.saga.pojos.PaymentRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class PaymentService {

    private static final PaymentsDAO paymentsDAO = new PaymentsDAO("jdbc:sqlite:food_delivery.db");

    public static Payment createPayment(PaymentRequest paymentRequest) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        Payment payment = new Payment();
        payment.setPaymentId(uuidAsString);
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setAmount(paymentRequest.getAmount());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(paymentRequest.getMethod().getType());
        paymentMethod.setDetails(paymentRequest.getMethod().getDetails());

        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(Payment.Status.PENDING);

        // Check if returned error is non-empty, i.e failure
        if (!paymentsDAO.insertPayment(payment).isEmpty()) {
            log.error("Failed to process payment for order {}", paymentRequest.getOrderId());
            payment.setErrorMsg("Payment creation failure");
            payment.setStatus(Payment.Status.FAILED);
        }
        else {
            if(makePayment(payment)) {
                payment.setStatus(Payment.Status.SUCCESSFUL);
            }
        }

        // Record final status
        String err = paymentsDAO.updatePayment(payment);
        if (!err.isEmpty()) {
            payment.setStatus(Payment.Status.FAILED);
        }

        return payment;
    }

    public static void cancelPayment(String orderId) {
        // Cancel Payment in DB
        Payment payment = new Payment();
        paymentsDAO.readPayment(orderId, payment);
        payment.setStatus(Payment.Status.CANCELED);
        paymentsDAO.updatePayment(payment);
    }

    private static boolean makePayment(Payment payment) {
        // Ideally an async call would be made with a callback
        // But, we're skipping that and assuming payment went through
        return true;
    }
}
