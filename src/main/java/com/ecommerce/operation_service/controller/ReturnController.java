package com.ecommerce.operation_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.operation_service.model.Order;
import com.ecommerce.operation_service.model.Return;
import com.ecommerce.operation_service.repository.OrderRepository;
import com.ecommerce.operation_service.repository.ReturnRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {
    
    @Autowired
    private ReturnRepository returnRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @GetMapping
    public ResponseEntity<List<Return>> getAllReturns() {
        return ResponseEntity.ok(returnRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Return> getReturnById(@PathVariable Long id) {
        return returnRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Return>> getReturnsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(returnRepository.findByOrderId(orderId));
    }
    
    @PostMapping
    public ResponseEntity<?> createReturn(@RequestBody Map<String, Object> returnRequest) {
        try {
            Long orderId = Long.parseLong(returnRequest.get("orderId").toString());
            String reason = returnRequest.get("reason").toString();
            String details = returnRequest.get("details").toString();
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new Exception("Order not found with id: " + orderId));
            
            Return returnObj = new Return();
            returnObj.setOrder(order);
            returnObj.setReturnDate(LocalDateTime.now());
            returnObj.setReason(reason);
            returnObj.setDetails(details);
            returnObj.setStatus("PENDING");
            
            Return savedReturn = returnRepository.save(returnObj);
            
            return ResponseEntity.ok(savedReturn);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReturnStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        try {
            String newStatus = statusUpdate.get("status");
            
            Return returnObj = returnRepository.findById(id)
                    .orElseThrow(() -> new Exception("Return not found with id: " + id));
            
            returnObj.setStatus(newStatus);
            Return updatedReturn = returnRepository.save(returnObj);
            
            return ResponseEntity.ok(updatedReturn);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}