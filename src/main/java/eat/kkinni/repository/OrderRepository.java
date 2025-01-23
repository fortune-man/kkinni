package eat.kkinni.repository;

import eat.kkinni.service.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

// src/main/java/eat/kkinni/repository/OrderRepository.java
public interface OrderRepository extends JpaRepository<Order, Long> {

}
