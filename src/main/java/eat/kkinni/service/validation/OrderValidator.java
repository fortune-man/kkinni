package eat.kkinni.service.validation;

import static eat.kkinni.service.validation.ErrorMessage.*;

import eat.kkinni.service.domain.Order;

// src/main/java/eat/kkinni/service/validation/OrderValidator.java
public class OrderValidator {
  public static void validate(Order order) {
    if (order.getUserName() == null || order.getMenuName() == null || order.getStatus() == null) {
      throw new IllegalArgumentException(MISSING_INPUT.getMessage());
    }
  }

  public static void validateStatus(String status) {
    if (status == null || status.isBlank()) {
      throw new IllegalArgumentException(MISSING_STATUS.getMessage());
    }
  }


}
