package eat.kkinni.service;

import static eat.kkinni.service.validation.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eat.kkinni.repository.OrderRepository;
import eat.kkinni.service.domain.Order;
import eat.kkinni.service.validation.ErrorMessage;
import java.util.List;
import java.util.Optional;
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

  @DisplayName("특정 ID로 주문 조회에 성공한다")
  @Test
  void findOrderById_success() {
    // Given
    Long orderId = 1L;
    Order order = new Order(orderId, "김주형", "닭가슴살 볶음밥", "준비중");
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    // When
    Order foundOrder = orderService.findOrderById(orderId);

    // Then
    assertNotNull(foundOrder);
    assertEquals(orderId, foundOrder.getId());
    assertEquals("김주형", foundOrder.getUserName());
    assertEquals("닭가슴살 볶음밥", foundOrder.getItem());
    assertEquals("준비중", foundOrder.getStatus());
    verify(orderRepository, times(1)).findById(orderId);
  }

  @DisplayName("존재하지 않는 ID로 주문 조회에 실패한다")
  @Test
  void findOrderById_failure_notFound() {
    // Given
    Long invalidOrderId = 99L;
    when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.findOrderById(invalidOrderId)
    );

    assertEquals(MISSING_ID.getMessage(), exception.getMessage());
    verify(orderRepository, times(1)).findById(invalidOrderId);
  }

  @DisplayName("데이터베이스에 저장된 모든 주문을 조회한다")
  @Test
  void findAllOrders_success() {
    // Given
    List<Order> orders = List.of(
        new Order(1L, "김주형", "닭가슴살 볶음밥", "준비중"),
        new Order(2L, "이진수", "햄치즈에그 샌드위치", "배달중"),
        new Order(3L, "노영지", "크랩샐러드", "배달완료")
    );
    when(orderRepository.findAll()).thenReturn(orders);

    // When
    List<Order> foundOrders = orderService.findAllOrders();

    // Then
    assertEquals(3, ((List<?>) foundOrders).size());
    assertEquals("김주형", foundOrders.get(0).getUserName());
    assertEquals("닭가슴살 볶음밥", foundOrders.get(0).getItem());
    assertEquals("준비중", foundOrders.get(0).getStatus());

    assertEquals("이진수", foundOrders.get(1).getUserName());
    assertEquals("햄치즈에그 샌드위치", foundOrders.get(1).getItem());
    assertEquals("배달중", foundOrders.get(1).getStatus());

    assertEquals("노영지", foundOrders.get(2).getUserName());
    assertEquals("크랩샐러드", foundOrders.get(2).getItem());
    assertEquals("배달완료", foundOrders.get(2).getStatus());

    verify(orderRepository, times(1)).findAll();
  }

  @DisplayName("주문 상태 수정에 성공한다")
  @Test
  void updateOrderStatus_success() {
    // Given
    Long orderId = 1L;
    String newStatus = "배달중";
    Order existingOrder = new Order(orderId, "김주형", "닭가슴살 볶음밥", "준비중");
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
    when(orderRepository.save(any(Order.class))).thenReturn(new Order(orderId, "김주형", "닭가슴살 볶음밥", newStatus));

    // When
    Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);

    // Then
    assertEquals(orderId, updatedOrder.getId());
    assertEquals(newStatus, updatedOrder.getStatus());
    verify(orderRepository, times(1)).findById(orderId);
    verify(orderRepository, times(1)).save(existingOrder);
  }

  @DisplayName("존재하지 않는 주문 ID로 조회 시 상태 업데이트에 실패한다")
  @Test
  void updateOrderStatus_failure_invalidId() {
    // Given
    Long invalidOrderId = 99L;
    String newStatus = "배달중";
    when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.updateOrderStatus(invalidOrderId, newStatus)
    );

    assertEquals(MISSING_ID.getMessage(), exception.getMessage());
    verify(orderRepository, times(1)).findById(invalidOrderId);
    verify(orderRepository, never()).save(any(Order.class));
  }

  @DisplayName("상세 주문 정보 수정에 성공하는지 테스트")
  @Test
  void updateOrderDetails_success() {
    // Given
    Long orderId = 1L;
    Order existingOrder = new Order(orderId, "김주형", "닭가슴살 볶음밥", "준비중");
    Order updatedOrder = new Order(orderId, "김이름", "새우볶음밥", "배달중");
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
    when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

    // When
    Order result = orderService.updateOrderDetails(orderId, updatedOrder);

    // Then
    assertEquals(orderId, result.getId());
    assertEquals("김이름", result.getUserName());
    assertEquals("새우볶음밥", result.getItem());
    assertEquals("배달중", result.getStatus());
    verify(orderRepository, times(1)).findById(orderId);
    verify(orderRepository, times(1)).save(existingOrder);
  }

  @DisplayName("필수 상태값 누락으로 수정 실패")
  @Test
  void updateOrder_failure_invalidStatus() {
    // Given
    Long orderId = 1L;
    String invalidStatus = null; // 상태값 누락
    Order existingOrder = new Order(orderId, "김주형", "닭가슴살 볶음밥", "준비중");

    when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.updateOrderStatus(orderId, invalidStatus)
    );

    assertEquals(MISSING_STATUS.getMessage(), exception.getMessage());

    // Verify no interactions with the repository due to invalid status
    verify(orderRepository, never()).findById(anyLong());
    verify(orderRepository, never()).save(any(Order.class)); // 저장 호출 금지
  }

}