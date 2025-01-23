package eat.kkinni.service;

import static eat.kkinni.service.validation.ErrorMessage.MISSING_ID;

import eat.kkinni.repository.OrderRepository;
import eat.kkinni.service.domain.Order;
import eat.kkinni.service.validation.ErrorMessage;
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

  public Order updateOrderStatus(Long orderId, String newStatus) {
    OrderValidator.validateStatus(newStatus); // 상태값 검증
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException(MISSING_ID.getMessage()));
    order.setStatus(newStatus);
    return orderRepository.save(order);
  }

  public Order updateOrderDetails(Long orderId, Order updatedOrder) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException(MISSING_ID.getMessage()));

    order.setUserName(updatedOrder.getUserName());
    order.setItem(updatedOrder.getItem());
    order.setStatus(updatedOrder.getStatus());

    return orderRepository.save(order);
  }
}
