package eat.kkinni.controller;

import eat.kkinni.service.OrderService;
import eat.kkinni.service.domain.Order;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.findAllOrders();
  }
}