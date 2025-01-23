package eat.kkinni.service;

import eat.kkinni.repository.OrderRepository;
import eat.kkinni.service.domain.Order;
import org.springframework.stereotype.Service;

// src/main/java/eat/kkinni/service/OrderService.java
@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public Order createOrder(Order order) {
    if (validate(order)) {
      throw new IllegalArgumentException("필수 입력값이 누락되었습니다.");
    }
    return orderRepository.save(order);
  }

  private static boolean validate(Order order) {
    return order.getUserName() == null || order.getItem() == null || order.getStatus() == null;
  }


}
