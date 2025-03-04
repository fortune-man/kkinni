package eat.kkinni.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import eat.kkinni.service.OrderService;
import eat.kkinni.service.domain.Order;
import java.util.List;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// src/test/java/eat/kkinni/controller/OrderControllerTest.java
class OrderControllerTest {

  private MockMvc mockMvc;

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
  }

  @Test
  @DisplayName("GET /orders - 성공적으로 모든 주문 조회")
  void getAllOrders_success() throws Exception {
    // Given
    List<Order> orders = List.of(
        new Order(1L, "김주형", "닭가슴살 볶음밥", "준비중"),
        new Order(2L, "이진수", "햄치즈에그 샌드위치", "배달중")
    );

    when(orderService.findAllOrders()).thenReturn(orders);

    // When & Then
    mockMvc.perform(get("/orders")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$[0].userName", is("김주형")))
        .andExpect(jsonPath("$[0].menuName", is("닭가슴살 볶음밥")))
        .andExpect(jsonPath("$[1].userName", is("이진수")))
        .andExpect(jsonPath("$[1].menuName", is("햄치즈에그 샌드위치")));
  }

  @Test
  @DisplayName("GET /orders/{id} - 특정 주문 조회 성공")
  void getOrderById_success() throws Exception {
    // Given
    Order order = new Order(1L, "김주형", "닭가슴살 볶음밥", "준비중");
    when(orderService.findOrderById(1L)).thenReturn(order);

    // When & Then
    mockMvc.perform(get("/orders/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.userName", is("김주형")))
        .andExpect(jsonPath("$.menuName", is("닭가슴살 볶음밥")))
        .andExpect(jsonPath("$.status", is("준비중")));
  }

  @Test
  @DisplayName("GET /orders/{id} - 존재하지 않는 주문 조회")
  void getOrderById_notFound() throws Exception {
    // Given
    when(orderService.findOrderById(999L)).thenReturn(null); // 존재하지 않는 ID

    // When & Then
    mockMvc.perform(get("/orders/999")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound()); // 404 기대
  }

  @Test
  @DisplayName("GET /orders/{id} - 유효하지 않은 ID로 요청하면 400 Bad Request를 반환한다")
  void getOrderById_invalidId_returnsBadRequest() throws Exception {
    mockMvc.perform(get("/orders/-1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /orders/{id} - 존재하지 않는 ID로 요청하면 404 Not Found를 반환한다")
  void getOrderById_notFound_returnsNotFound() throws Exception {
    when(orderService.findOrderById(999L)).thenReturn(null);

    mockMvc.perform(get("/orders/999")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

}