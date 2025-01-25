import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50, // 동시 접속 사용자 수
  duration: '5s', // 테스트 실행 시간
};

export default function () {
  const res = http.get('http://localhost:8080/orders/1'); // API 엔드포인트
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}
