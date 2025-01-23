package eat.kkinni.service.validation;

// src/main/java/eat/kkinni/service/validation/ErrorMessage.java
public enum ErrorMessage {
  MISSING_INPUT("필수 입력값이 누락되었습니다."),
  MISSING_USER_NAME("사용자 이름이 누락되었습니다."),
  MISSING_ITEM("메뉴 항목이 누락되었습니다."),
  MISSING_STATUS("주문 상태가 누락되었습니다.");


  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
