package eat.kkinni.controller;

import eat.kkinni.service.OrderService;
import eat.kkinni.service.domain.Order;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// src/main/java/eat/kkinni/controller/OrderController.java
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  // GET /orders - 모든 주문 조회
  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.findAllOrders();
  }

  // GET /orders/{id} - 특정 주문 조회
  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
    Order order = orderService.findOrderById(id);
    if (order != null) {
      return ResponseEntity.ok(order); // 200 OK 응답
    }
    return ResponseEntity.notFound().build(); // 404 Not Found 응답
    }
  }