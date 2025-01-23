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
import org.springframework.boot.test.mock.mockito.MockBean;
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
    List<Order> orders = List.of(
        new Order(1L, "김주형", "닭가슴살 볶음밥", "준비중"),
        new Order(2L, "이진수", "햄치즈에그 샌드위치", "배달중")
    );

    when(orderService.findAllOrders()).thenReturn(orders);

    mockMvc.perform(get("/orders")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$[0].userName", is("김주형")))
        .andExpect(jsonPath("$[1].userName", is("이진수")));
  }
}