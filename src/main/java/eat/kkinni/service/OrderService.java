package eat.kkinni.service;

import eat.kkinni.repository.OrderRepository;
import eat.kkinni.service.domain.Order;
import eat.kkinni.service.validation.OrderValidator;
import java.util.List;
import org.springframework.stereotype.Service;

// src/main/java/eat/kkinni/service/OrderService.java
@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public Order createOrder(Order order) {
    OrderValidator.validate(order);
    return orderRepository.save(order);
  }

  private static boolean validate(Order order) {
    return order.getUserName() == null || order.getItem() == null || order.getStatus() == null;
  }

  public Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 주문이 존재하지 않습니다."));
  }

  public List<Order> findAllOrders() {
    return orderRepository.findAll();
  }
}
