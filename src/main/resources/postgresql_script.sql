-- DB 초기화 시 테이블 drop

-- DROP TABLE IF EXISTS public.bankstatement;
-- DROP TABLE IF EXISTS public.currency;
-- DROP TABLE IF EXISTS public."instance";
-- DROP TABLE IF EXISTS public."member";
-- DROP TABLE IF EXISTS public.trade;
-- DROP TABLE IF EXISTS public."order";
-- DROP TABLE IF EXISTS public.wallet;
--drop type IF EXISTS role cascade;
--drop type IF EXISTS yn cascade;

-- DB 생성

CREATE TYPE role AS ENUM ('ADMIN', 'NORMAL');
CREATE TYPE yn AS ENUM ('Y', 'N');

CREATE TABLE bankstatement (
	"date" timestamptz NOT NULL,
	member_id varchar(100) NOT NULL,
	order_type bpchar(5) NOT NULL,
	bank varchar(20) NOT NULL,
	krw int8 NOT NULL,
	CONSTRAINT bankstatement_pk PRIMARY KEY (date, member_id)
);

COMMENT ON COLUMN public.bankstatement."date" IS '입출금 거래 시간';
COMMENT ON COLUMN public.bankstatement.member_id IS '회원 아이디';
COMMENT ON COLUMN public.bankstatement.order_type IS '입출금 구분';
COMMENT ON COLUMN public.bankstatement.bank IS '은행';
COMMENT ON COLUMN public.bankstatement.krw IS '원화';

CREATE TABLE currency (
	currency varchar(100) NOT NULL,
	currency_kr varchar(100) NOT NULL,
	currency_abbr varchar(5) NOT NULL,
	currenct_price int8 NOT NULL,
	previous_price int8 NOT NULL,
	transaction_price int8 NOT NULL,
	CONSTRAINT currency_pkey PRIMARY KEY (currency)
);
CREATE INDEX currency_currency_abbr_idx ON public.currency USING btree (currency_abbr);
CREATE INDEX currency_currency_kr_idx ON public.currency USING btree (currency_kr);

COMMENT ON COLUMN public.currency.currency IS '화폐명';
COMMENT ON COLUMN public.currency.currency_kr IS '화페 한글명';
COMMENT ON COLUMN public.currency.currency_abbr IS '화폐 약어';
COMMENT ON COLUMN public.currency.currenct_price IS '현재가';
COMMENT ON COLUMN public.currency.previous_price IS '전일가';
COMMENT ON COLUMN public.currency.transaction_price IS '거래대금';


CREATE TABLE "instance" (
	"date" timestamptz NOT NULL,
	system_id varchar(100) NOT NULL,
	status bpchar(5) NOT NULL,
	CONSTRAINT instance_pkey PRIMARY KEY (date)
);

COMMENT ON COLUMN public."instance"."date" IS '시스템 확인 날짜';
COMMENT ON COLUMN public."instance".system_id IS '시스템 아이디';
COMMENT ON COLUMN public."instance".status IS '시스템 상태';


CREATE TABLE "member" (
	member_id varchar(100) NOT NULL,
	"role" role NOT NULL,
	use_yn yn NOT NULL,
	reg_date timestamptz NOT NULL,
	upt_date timestamptz NOT NULL,
	"password" varchar(100) NOT NULL,
	CONSTRAINT member_pkey PRIMARY KEY (member_id)
);
CREATE INDEX member_password_idx ON public.member USING btree (password);

COMMENT ON COLUMN public."member".member_id IS '회원 아이디';
COMMENT ON COLUMN public."member"."role" IS '비밀번호';
COMMENT ON COLUMN public."member".use_yn IS '역할';
COMMENT ON COLUMN public."member".reg_date IS '사용여부';
COMMENT ON COLUMN public."member".upt_date IS '가입시간';
COMMENT ON COLUMN public."member"."password" IS '수정시간';


CREATE TABLE trade (
	order_id bigserial NOT NULL,
	trade_date timestamptz NOT NULL,
	buy_id int8 NOT NULL,
	sell_id int8 NOT NULL,
	CONSTRAINT trade_pkey PRIMARY KEY (order_id)
);

COMMENT ON COLUMN public.trade.order_id IS '거래 아이디';
COMMENT ON COLUMN public.trade.trade_date IS '거래 시간';
COMMENT ON COLUMN public.trade.buy_id IS '구매 주문 아이디';
COMMENT ON COLUMN public.trade.sell_id IS '판매 주문 아이디';

CREATE TABLE "order" (
	order_id bigserial NOT NULL,
	order_date timestamptz NOT NULL,
	order_member varchar(100) NOT NULL,
	currency varchar(100) NOT NULL,
	order_type bpchar(5) NOT NULL,
	price int8 NOT NULL,
	quantity int8 NOT NULL,
	trade_id int8 NULL,
	CONSTRAINT order_pkey PRIMARY KEY (order_id)
);


COMMENT ON COLUMN public.order.order_id IS '아이디';
COMMENT ON COLUMN public.order.order_date IS '주문 시간';
COMMENT ON COLUMN public.order.order_member IS '주문자 아이디';
COMMENT ON COLUMN public.order.currency IS '화폐 종류';
COMMENT ON COLUMN public.order.order_type IS '주문 종류';
COMMENT ON COLUMN public.order.price IS '주문지정금액';
COMMENT ON COLUMN public.order.quantity IS '주문량';
COMMENT ON COLUMN public.order.trade_id IS '거래 아이디';


CREATE TABLE wallet (
	member_id varchar(100) NOT NULL,
	currency varchar(100) NOT NULL,
	quantity int8 NOT NULL,
	average_price int8 NOT NULL,
	CONSTRAINT wallet_pk PRIMARY KEY (member_id, currency)
);

COMMENT ON COLUMN public.wallet.member_id IS '회원 아이디';
COMMENT ON COLUMN public.wallet.currency IS '화폐 종류';
COMMENT ON COLUMN public.wallet.quantity IS '보유량';
COMMENT ON COLUMN public.wallet.average_price IS '매수 평균가';

