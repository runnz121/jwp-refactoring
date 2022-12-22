# 키친포스

## 요구 사항

### 상품

- 구성요소
  - 이름
  - 가격
- 상품을 등록할 수 있다
  - 가격이 없거나 0 미만인 것은 등록할 수 없다
- 전체 상품을 조회할 수 있다

### 메뉴그룹

- 구성요소
  - 이름
- 메뉴그룹을 등록할 수 있다
- 전체 메뉴그룹을 조회할 수 있다

### 메뉴

- 구성요소
  - 이름
  - 가격
  - 메뉴그룹 id
  - 메뉴상품 목록
- 메뉴를 등록할 수 있다
  - 가격이 없거나 0 미만인 것은 등록할 수 없다
  - 메뉴그룹 id 는 등록된 것이어야한다
  - 메뉴상품 목록의 상품은 등록된 것이어야 한다
  - 가격은 메뉴상품의 총합계 금액보다 작아야한다
- 전체 메뉴를 조회할 수 있다
  - 메뉴의 구성요소인 메뉴상품 목록도 모두 조회가 된다

### 메뉴상품

- 구성요소
  - 메뉴 id
  - 상품 id
  - 수량

### 주문테이블

- 구성요소
  - 테이블그룹 id
  - 방문한손님 수
  - 빈 테이블 여부
- 주문테이블을 등록할 수 있다
  - 테이블그룹 id 를 초기화하고 등록한다
- 전체 주문테이블을 조회할 수 있다
- 빈 테이블 여부를 변경할 수 있다
  - 존재하는 주문테이블 id 여야한다
  - 테이블그룹 id 가 존재할 때 변경할 수 없다
  - 요리중이거나 식사중인 주문이 있는 주문테이블은 변경할 수 없다
- 방문한손님 수를 변경할 수 있다
  - 변경 방문한손님 수가 0 미만이면 변경할 수 없다
  - 존재하는 주문테이블 id 여야한다
  - 빈 테이블은 변경할 수 없다

### 단체지정

- 구성요소
  - 주문테이블 목록
  - 생성시각
- 단체지정 할 수 있다
  - 주문테이블 목록이 비어있거나 1개인 경우 지정할 수 없다
  - 등록된 주문테이블만 지정할 수 있다
  - 채워진 주문테이블이거나 이미 단체지정된 주문테이블은 지정할 수 없다
- 단체지정 해제할 수 있다
  - 요리중, 식사중인 주문이 있는 주문테이블은 단체지정을 취소할 수 없다

### 주문

- 구성요소
  - 주문테이블 id
  - 주문상태
  - 주문시간
  - 주문항목 목록
- 주문을 등록할 수 있다
  - 주문항목 목록은 비어있지 않아야한다
  - 주문항목 목록의 모든 메뉴는 등록되어있어야한다
  - 주문테이블은 등록되어있야하고 비어있어야한다
- 전체 주문을 조회할 수 있다
  - 주문항목 목록을 포함하여 조회한다
- 주문상태를 변경할 수 있다
  - 완료된 주문은 변경할 수 없다

### 주문항목

- 구성요소
  - 주문 id
  - 메뉴 id
  - 수량

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

### Step 요구사항

## step1

-[x] 키친포스의 요구사항을 작성한다
-[x] 위에서 정리한 요구사항을 토대로 테스트코드 작성
  -[x] 모든 `Business Object` 에 대해 테스트코드 작성

## step2

-[x] 단위테스트하기 어려운 코드와 가능한 코드를 분리 후 단위테스트 가능한 코드에 단위테스트 구현
  -[x] Spring Data JPA 사용 시 `spring.jpa.hibernate.ddl-auto=validate` 옵션을 필수
  -[x] 객체지향 생활체조원칙 준수

## step3

-[x] 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
-[x] 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.

## step4

-[x] Gradle 의 멀티 모듈 개념을 적용해 자유롭게 서로 다른 프로젝트로 분리해 본다.
