package eat.kkinni.service;

import static eat.kkinni.service.validation.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eat.kkinni.repository.OrderRepository;
import eat.kkinni.service.domain.Order;
import eat.kkinni.service.validation.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// src/test/java/eat/kkinni/service/OrderServiceTest.java
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderService orderService;

  public OrderServiceTest() {
    MockitoAnnotations.openMocks(this); // 초기화
  }

  @DisplayName("필수 값 입력이 정상적일 경우 주문 생성에 성공한다")
  @Test
  void createOrder_success() {
    // Given
    Order order = new Order(null, "김주형", "닭가슴살 볶음밥", "준비중");
    when(orderRepository.save(any(Order.class))).thenReturn(new Order(1L, "김주형", "닭가슴살 볶음밥", "준비중"));

    // When
    Order createdOrder = orderService.createOrder(order);

    // Then
    assertNotNull(createdOrder.getId());
    assertEquals("김주형", createdOrder.getUserName());
    assertEquals("닭가슴살 볶음밥", createdOrder.getItem());
    assertEquals("준비중", createdOrder.getStatus());
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @DisplayName("필수 입력값을 누락하면 주문 생성에 실패한다")
  @Test
  void createOrder_failure_missingRequiredFields() {
    // Given
    Order invalidOrder = new Order(null, null, null, null); // 필수 값 전부 누락

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrder(invalidOrder)
    );

    assertEquals(MISSING_INPUT.getMessage(), exception.getMessage());
    verify(orderRepository, never()).save(any(Order.class)); // Repository는 호출되지 않아야 함
  }
}