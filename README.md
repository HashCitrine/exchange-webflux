# Exchange-Eureka

가상 화폐 거래소 컨셉의 MSA 서비스 구현 토이 프로젝트

## Developer

- 👩‍💻 [HashCitrine](https://github.com/HashCitrine) (`webflux, oauth, MSA` 구현 및 개발 흐름 기획)
- 👩‍💻 [Soo-ss](https://github.com/Soo-ss) (`api, oauth, wallet` 구현)

## 사용 기술

- `Spring Boot` : WebFlux, Eureka
- `DB` : PostgreSQL, Redis
- `Message Queue `: Kafka

※ Kafka는 Docker container 이용.

## 개발한 서버 목록

- [exchange-api](https://github.com/HashCitrine/exchange-api)
- [exchange-oauth](https://github.com/HashCitrine/exchange-oauth)
- [exchange-eureka](https://github.com/HashCitrine/exchange-eureka)
- [exchange-gateway](https://github.com/HashCitrine/exchange-gateway)
- [exchange-webflux](https://github.com/HashCitrine/exchange-webflux)
- [exchange-wallet](https://github.com/HashCitrine/exchange-wallet)

😀 `exchange-api, exchange-oauth, exchange-wallet, exchange-webflux` 4개의 서버를 관리하기 위해, `exchange-eureka, exchange-gateway`를 통하여 MSA를 구현했습니다.

## 거래 처리 (submitOrder)
exchange-oauth에서 토큰 검증 후 kafka를 통해 전달 받은 거래건(`submitOrder`) 처리 진행  
(거래 처리를 위한 토큰 검증 과정은 [exchange-oauth](https://github.com/HashCitrine/exchange-oauth) 참고)


### 1. DB 구조
![DB](https://github.com/HashCitrine/exchange-webflux/blob/master/img/DB.png?raw=true)

- order
  ![order](https://github.com/HashCitrine/exchange-webflux/blob/master/img/order.png?raw=true)

- trade
  ![trade](https://github.com/HashCitrine/exchange-webflux/blob/master/img/trade.png?raw=true)

- wallet
  ![wallet](https://github.com/HashCitrine/exchange-webflux/blob/master/img/trade.png?raw=true)

- currency
  ![currency](https://github.com/HashCitrine/exchange-webflux/blob/master/img/trade.png?raw=true)

- bankstatement
  ![bankstatement](https://github.com/HashCitrine/exchange-webflux/blob/master/img/trade.png?raw=true)


---

### 2. 거래 처리 로직
1. exchange-oauth에서 허용된 거래건 입수(`submitOrder`) 후 거래 처리 로직 호출(`ExchangeService.exchange`)
2. 현재가 업데이트 진행(`currencyRepository.updateCurrentPrice`) 후 실제 거래 로직 진행(`ExchangeService.trade`)
3. 거래 대상 조회(`TradeRepository.findTradeOrder`)  
   (1) 거래 처리 가능한 건이 없을 경우 : 로직 종료  
   (2) 거래 가능한 건을 발견한 경우 : 거래 처리(`tradeRepository.updateSuccessOrder`)
---

### 3. ExchangeService
작성 중...

---
### ※ 차후 개선점
1. '현재가 반영'이 **거래 요청 입수되었을 때만** 진행하도록 구성되어 있음
2. 네트워크 등의 문제로 서비스 요청이 제대로 이루어지지 못한 경우에도 소급하여 처리 가능하도록 시스템 설계 필요  
   ► 배치 등을 활용해 해당 상황을 주기적으로 업데이트 되는 서비스 필요(exchange-batch...)


