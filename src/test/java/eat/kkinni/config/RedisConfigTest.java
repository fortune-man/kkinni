package eat.kkinni.config;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

class RedisConfigTest {
  final RedisConfig redisConfig = new RedisConfig();
  void codeForFun() {
    String string = redisConfig.toString();
    System.out.println(string);

  }
}