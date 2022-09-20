# Exchange-Webflux

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

``` java
public class TradeConsumer {

    private final KafkaProducer producer;
    private final ExchangeService exchangeService;

    @KafkaListener(topics = "submitOrder", groupId = "exchange")
    public void consume(String message) throws IOException {
        log.info("Consumed message : {}", message);

        Map<String, String> json = new Gson().fromJson(message, Map.class);
        String id = json.get("message");
        int interval = 1;

        log.info(">>> start exchange");
        exchangeService.exchange(Long.parseLong(message))
                .subscribe();
    }
}
```

### 1. DB 구조
![DB](https://github.com/HashCitrine/exchange-webflux/blob/master/img/DB.png?raw=true)

- order
![order](https://github.com/HashCitrine/exchange-webflux/blob/master/img/order.png?raw=true)
거래 요청이 등록될 때 정보 등록 후 거래 처리에 따라 stock, status, upt_date 등의 값 변경
- trade
![trade](https://github.com/HashCitrine/exchange-webflux/blob/master/img/trade.png?raw=true)
실제 거래가 이루어질 경우 거래 내역 정보 등록

- wallet
![wallet](https://github.com/HashCitrine/exchange-webflux/blob/master/img/wallet.png?raw=true)
서비스 이용자들의 잔고 (거래를 위해 충전한 금액 및 가상 화폐 포함)

- currency
![currency](https://github.com/HashCitrine/exchange-webflux/blob/master/img/currency.png?raw=true)  
서비스 내에 이용가능한 각종 가상 화폐 정보 관리

- bankstatement
![bankstatement](https://github.com/HashCitrine/exchange-webflux/blob/master/img/bankstatement.png?raw=true)  
거래를 위한 금액 충전(입금) 및 출금 내역 관리

---

### 2. 거래 처리 로직
1. exchange-oauth에서 허용된 거래건 입수(`submitOrder`) 후 거래 처리 로직 호출(`ExchangeService.exchange`)
2. 현재가 업데이트 진행(`currencyRepository.updateCurrentPrice`) 후 실제 거래 로직 진행(`ExchangeService.trade`)
3. 거래 대상 조회([`TradeRepository.findTradeOrder`]())  
   (1) 거래 처리 가능한 건이 없을 경우 : 로직 종료  
   (2) 거래 가능한 건을 발견한 경우 : 거래 처리(`tradeRepository.updateSuccessOrder`)
---

### 3. ExchangeService 상세
위 2번의 거래 처리 로직에 연관하여 소스 내 주석으로 로직 상세내용 설명
``` java
public Mono<Trade> exchange(Long orderId) {
        log.info(">>> exchange process");

        // 1. submitOrder를 통해 전달 받은 거래건의 PK를 통해 거래 대상 화폐 정보 조회 (Currency 테이블)
        return currencyRepository.findOrderCurrency(orderId)
                .flatMap(currency -> {
                    log.info("currency : " + currency);
                    // 2. 현재가 계산
                    return currencyRepository.currentPrice(currency)
                            .flatMap(currentPrice -> {
                                // 2. 현재가 업데이트
                                currencyRepository.updateCurrentPrice(currentPrice.getCurrentPrice(), currentPrice.getCurrency()).subscribe();
                                // 3. 거래 진행 ▶︎ 아래의 trade 메소드에서 처리
                                return trade(currentPrice, 1);
                            })
                            .publishOn(Schedulers.newElastic(currency));
                });

      // 3. 거래 진행
    public Mono<Trade> trade(Currency currency, int i) {
        log.info(">>> find trade target");
        // 3. 거래 가능한 대상 조회 (Order 테이블)
        return tradeRepository.findTradeOrder(currency.getCurrency(), currency.getCurrentPrice())
                .flatMap(tradeInfo -> {
                    log.info(">>> buyOrderId : " + tradeInfo.getBuyOrderId() + ", sellOrderId : " + tradeInfo.getSellOrderId() + ", stock : " + tradeInfo.getQuantity());
                    // 1) 조건에 맞는 매수, 매도자가 없을 경우 로직 종료
                    if(tradeInfo.getBuyOrderId() == null || tradeInfo.getSellOrderId() == null){
                            return Mono.just(new Trade());
//                        }
                    } else {
                        log.info(">>> success find trade target! try trade...");
                        // 2) 조건에 맞는 매수, 매도자가 있을 경우 : trade 테이블에 insert & 거래된만큼 stock량 줄이기
                        tradeRepository.updateSuccessOrder(tradeInfo.getBuyOrderId(), tradeInfo.getSellOrderId(), tradeInfo.getQuantity()).subscribe();
                        return tradeRepository.insertTrade(tradeInfo.getBuyOrderId(), tradeInfo.getSellOrderId(), tradeInfo.getQuantity());
                    }
                });
    }
```


---
### ※ 차후 개선점
1. '현재가 반영'이 **거래 요청 입수되었을 때만** 진행하도록 구성되어 있음  
2. 네트워크 등의 문제로 서비스 요청이 제대로 이루어지지 못한 경우에도 소급하여 처리 가능하도록 시스템 설계 필요  
    ► 배치 등을 활용해 해당 상황을 주기적으로 업데이트 되는 서비스 필요(exchange-batch...)
3. WebFlux 이해도 부족으로 클린 코드 작성 및 검증이 이루어지지 않음
    ► 각종 테스트 및 개발 경험, 자료형(Mono, Flux 등)의 공부 필요


