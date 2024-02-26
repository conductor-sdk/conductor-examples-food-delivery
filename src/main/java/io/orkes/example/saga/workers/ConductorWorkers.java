package io.orkes.example.saga.workers;

import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import io.orkes.example.saga.pojos.*;
import io.orkes.example.saga.service.InventoryService;
import io.orkes.example.saga.service.OrderService;
import io.orkes.example.saga.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
@ComponentScan(basePackages = {"io.orkes"})
public class ConductorWorkers {
    /**
     * Note: Using this setting, up to 5 tasks will run in parallel, with tasks being polled every 200ms
     */
    @WorkerTask(value = "order_food", threadCount = 3, pollingInterval = 300)
    public TaskResult orderFoodTask(OrderRequest orderRequest) {
        String orderId = OrderService.createOrder(orderRequest);

        TaskResult result = new TaskResult();
        Map<String, Object> output = new HashMap<>();

        if(orderId != null) {
            output.put("orderId", orderId);
            result.setOutputData(output);
            result.setStatus(TaskResult.Status.COMPLETED);
        } else {
            output.put("orderId", null);
            result.setStatus(TaskResult.Status.FAILED);
        }
        return result;
    }

    @WorkerTask(value = "check_inventory", threadCount = 2, pollingInterval = 300)
    public TaskResult checkInventoryTask(CheckInventoryRequest checkInventoryRequest) {
        int restaurantId = checkInventoryRequest.getRestaurantId();
        ArrayList<FoodItem> items = checkInventoryRequest.getItems();
        boolean availability = InventoryService.checkAvailability(restaurantId, items);

        TaskResult result = new TaskResult();

        if (availability) {
            result.setStatus(TaskResult.Status.COMPLETED);
        } else {
            result.setReasonForIncompletion("Restaurant is closed");
            result.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
        }

        return result;
    }

    @WorkerTask(value = "make_payment", threadCount = 2, pollingInterval = 300)
    public TaskResult makePaymentTask(PaymentRequest paymentRequest) {
        TaskResult result = new TaskResult();

        Payment payment = PaymentService.createPayment(paymentRequest);
        Map<String, Object> output = new HashMap<>();
        output.put("orderId", payment.getOrderId());
        output.put("paymentId", payment.getPaymentId());
        output.put("paymentStatus", payment.getStatus().name());

        if(payment.getStatus() == Payment.Status.SUCCESSFUL) {
            result.setStatus(TaskResult.Status.COMPLETED);
        } else {
            output.put("error", payment.getErrorMsg());
            result.setStatus(TaskResult.Status.FAILED);
        }

        result.setOutputData(output);

        return result;
    }
//
//    @WorkerTask(value = "confirm_order", threadCount = 2, pollingInterval = 300)
//    public Map<String, Object> checkForOrderConfirmation(CheckInventoryRequest orderConfirmationReq) {
//        Map<String, Object> result = new HashMap<>();
//        Order order = OrderService.getOrder(orderConfirmationReq.getOrderId());
//        OrderService.confirmOrder(order);
//        return result;
//    }
//
    @WorkerTask(value = "notify_driver", threadCount = 2, pollingInterval = 300)
    public Map<String, Object> checkForDriverNotifications(DriverNotificationRequest driverNotificationRequest) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }

    @WorkerTask(value = "notify_customer", threadCount = 2, pollingInterval = 300)
    public Map<String, Object> checkForCustomerNotifications(Order order) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }
//
    @WorkerTask(value = "cancel_payment", threadCount = 2, pollingInterval = 300)
    public Map<String, Object> cancelPaymentTask(CancelPaymentRequest cancelPaymentRequest) {
        Map<String, Object> result = new HashMap<>();
        PaymentService.cancelPayment(cancelPaymentRequest.getOrderId());
        return result;
    }
//
//    @WorkerTask(value = "cancel_driver_assignment", threadCount = 2, pollingInterval = 300)
//    public Map<String, Object> checkForDriverAssignmentCancellations(CheckInventoryRequest driverCancellationReq) {
//        Map<String, Object> result = new HashMap<>();
//        ShipmentService.cancelAssignment(driverCancellationReq.getOrderId());
//        return result;
//    }
//
    @WorkerTask(value = "cancel_order", threadCount = 2, pollingInterval = 300)
    public Map<String, Object> cancelOrderTask(CancelOrderRequest cancelOrderRequest) {
        Map<String, Object> result = new HashMap<>();
        Order order = OrderService.getOrder(cancelOrderRequest.getOrderId());
        OrderService.cancelOrder(order);
        return result;
    }

}
