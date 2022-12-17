# 키친포스

## 요구 사항

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

## 키친포스 요구사항

* 상품
    * 생성
        * `상품`의 가격은 0 이상이어야 한다
        * `상품` 이름은 중복이 가능하다
    * 목록 조회
        * `상품` 목록이 조회 된다
* 메뉴그룹
    * 성격
        * `메뉴그룹`은 여러 `메뉴`를 포함하는 카테고리 성격의 데이터 이다
    * 생성
        * `메뉴그룹`이 생성 된다
    * 조회
        * `메뉴그룹` 목록이 조회 된다
* 메뉴
    * 생성
        * `메뉴`의 가격은 0 이상이어야 한다
        * `메뉴`는 하나의 `메뉴그룹`에 반드시 포함 되어야 한다
            * `메뉴그룹`은 `메뉴` 생성 이전에 반드시 존재해야 한다
        * `메뉴`는 여러개의 `메뉴상품` 을 가질 수 있어야 한다
            * `상품`은 `메뉴` 생성 이전에 반드시 존재해야 한다
            * `메뉴상품`은 `메뉴` 생성 이후에 함께 생성 된다
        * `메뉴`의 `가격`은 메뉴에 포함 된 `상품 목록`의 `가격` 합과 같거나 더 작아야한다
    * 목록 조회
        * `메뉴 목록`이 조회 된다
* 주문
    * 생성
        * 하나 이상의 `주문 항목`을 가져야 한다
        * `주문`에 포함 된`주문 항목`의 갯수는 `주문 항목`에 포함 된 `메뉴`의 갯수와 일치해야 한다
        * `주문 테이블`은 주문 이전에 반드시 존재해야 한다
        * `주문` 생성 시점에 `주문 테이블`은 `빈 테이블`이 아니어야한다
        * `주문`을 생성하면 `주문 상태`는 `조리`상태가 된다
    * 목록 조회
        * `주문` 목록을 조회하면 `주문 항목` 목록도 함께 조회 된다
    * 주문 상태 변경
        * `주문 상태`를 변경하고자 하는 `주문`은 반드시 존재해야 한다
        * `주문 상태`를 변경하고자 하는 `주문`의 상태는 `계산 완료` 상태가 아니어야 한다
* 주문 테이블
    * 생성
        * `주문 테이블`을 생성한다
    * 목록 조회
        * `주문 테이블` 목록을 조회한다
    * 빈 테이블로 변경
        * `주문 테이블`의 상태를 `빈 테이블` 로 변경한다
        * `빈 테이블로 변경`을 수행하려는 `주문 테이블`은 반드시 존재해야 한다
        * `주문 테이블`에 속한 `주문`의 `주문 상태`는 `계산 완료` 상태어야 한다.
    * 방문 손님 수 변경
        * `방문 손님`의 수는 1명 이상이어야 한다
        * `방문 손님` 수를 변경하려는 `주문 테이블`은 반드시 존재해야 한다
        * `주문 테이블`은 비어 있으면 안된다
        * (방문 손님 수를 변경한다)
* 단체 지정
    * 생성
        * `단체 지정` 요청 `주문 테이블`이 비어 있으면 안 된다
        * `단체 지정` 요청 `주문 테이블`의 수는 2 이상이어야 한다
        * `단체 지정` 요청 `주문 테이블`은 실제 존재하는 `주문 테이블`의 수와 일치 해야 한다
        * `단체 지정` 요청 `주문 테이블`은 `다른 단체 지정`에 속해 있으면 안된다
    * 단체 지정 해제
        * `단체 지정`에 속해 있는 모든 `주문`의 (상태)는 (결제 완료) 상태어야 한다

# 2단계 - 서비스 리펙터링

## 요구 사항

* 단위 테스트하기 어려운 코드와 `단위 테스트 가능한 코드`를 `분리`하고, `단위 테스트 가능한 코드`에 대해 `단위 테스트를 구현`한다
* JPA 구현체인 Spring Data Jpa로의 코드 마이그레이션을 수행한다
* 현재 테스트의 보호를 받고 있는 상태로, 테스트의 보호 속에서 4-step 으로 리펙터링을 진행한다
    1. `Application Layer`에 존재하는 비즈니스 로직(생성과 같은 단순한 기능)을 `Domain Layer`로 옮기기 (소극적 기능 리펙터링)(TDD)
    2. `Domain Layer` 와 `Infra Layer` 간의 의존 구조 개선하기
        * JPA 구현체인 Spring-Data-Jpa 도입
    3. `Infra Layer`의 보호 아래 `도메인 객체`를 세부적으로 분리 (적극적 기능 리펙터링)
        * 래핑 클래스, 일급 컬렉션 등 활용
    4. 패키지 분리

### 힌트
* 테스트하기 쉬운 부분과 어려운 부분을 분리
* 한 번에 완벽한 설계를 하겠다는 욕심을 버려라
* 모델에 setter 메서드 넣지 않기
