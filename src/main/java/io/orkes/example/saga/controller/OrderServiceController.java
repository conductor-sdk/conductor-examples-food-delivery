package io.orkes.example.saga.controller;

import io.orkes.example.saga.pojos.OrderRequest;
import io.orkes.example.saga.service.WorkflowService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
public class OrderServiceController {

    private final WorkflowService workflowService;

    @PostMapping(value = "/triggerRideOrderFlow", produces = "application/json")
    public ResponseEntity<Map<String, Object>> triggerRideOrderFlow(@RequestBody OrderRequest orderRequest) {
        log.info("Starting ride order flow for: {}", orderRequest);
        return ResponseEntity.ok(workflowService.startFoodDeliveryWorkflow(orderRequest));
    }
}
