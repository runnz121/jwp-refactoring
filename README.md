# 키친포스

## 요구 사항
### 상품
- 상품을 등록할 수 있다.
  - 상품명, 가격을 입력받아 등록. 
  - 상품의 가격은 0보다 작을수 없다.
- 상품 목록을 조회할 수 있다.

### 메뉴 그룹
- 메뉴 그룹을 등록할 수 있다.
  - 메뉴 그룹 이름을 입력받아 등록.
- 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴
- 메뉴를 등록할 수 있다.
  - 메뉴 이름, 가격, 메뉴 그룹 ID, 메뉴 상품 목록을 입력 받아 등록.
  - 메뉴의 가격이 없거나, 0보다 작을 경우 등록할 수 없다.
  - 메뉴 그룹 ID에 해당하는 메뉴 그룹이 없을 경우 등록할 수 없다.
  - 메뉴 상품 목록에 해당하는 상품이 없을 경우 등록할 수 없다.
  - 메뉴 등록 가격이 원래 상품 가격의 합(금액)보다 클 경우 등록할 수 없다.
  - 메뉴를 등록할 때 메뉴 상품들이 같이 등록된다.
- 메뉴 목록을 조회할 수 있다.
  - 이때 메뉴 상품 목록도 같이 조회된다.

### 주문
- 주문을 등록할 수 있다.
  - 주문 테이블 ID, 주문 항목 목록을 입력 받아 등록.
  - 주문시 주문 항목 목록이 비어있을 경우 등록할 수 없다.
  - 주문 항목 목록에 등록되어 있지 않은 메뉴가 있을 경우 등록할 수 없다.
  - 주문 테이블 ID에 해당하는 주문 테이블이 없을 경우 경우 등록할 수 없다.
  - 주문 테이블 ID에 해당하는 주문 테이블이 빈 테이블 상태일 경우 등록할 수 없다.
  - 주문시 주문 상태는 `조리` 상태가 된다.
  - 주문 등록시 주문 항목들도 같이 등록된다.
- 주문 목록을 조회할 수 있다.
  - 이때 주문 항목 목록도 같이 조회된다.
- 주문 상태를 변경할 수 있다.
  - 주문 ID, 변경할 주문 상태를 입력 받아 변경.
  - 주문 ID에 해당하는 주문이 없을 경우 변경할 수 없다.
  - 이미 주문의 상태가 `완료`인 경우 변경할 수 없다.

### 주문 테이블
- 주문 테이블을 등록할 수 있다.
  - 방문한 손님수, 빈 테이블 상태를 입력받아 등록.
- 주문 테이블 목록을 조회할 수 있다.
- 주문 테이블의 빈 테이블 상태를 변경할 수 있다.
  - 테이블 ID, 빈 테이블 상태을 입력받아 변경.
  - 입력받은 주문 테이블 ID에 해당하는 주문 테이블이 없을 경우 변경할 수 없다.
  - 해당 주문 테이블이 단체 지정이 이미 되어있는 경우 상태를 변경할 수 없다.
  - 주문 테이블에 해당하는 주문의 상태가 `조리중` 또는 `식사중` 일 경우 변경할 수 없다.
- 주문 테이블의 방문한 손님수를 변경할 수 있다.
  - 테이블 ID, 방문한 손님수를 입력받아 변경.
  - 입력받은 방문한 손님수가 0보다 작을 경우 변경할수 없다.
  - 입력받은 주문 테이블 ID에 해당하는 주문 테이블이 없을 경우 변경할 수 없다.
  - 입력받은 주문 테이블 ID에 해당하는 주문 테이블의 상태가 빈 테이블 상태일 경우 변경할 수 없다.

### 단체 지정
- 단체 지정을 등록할 수 있다.
  - 주문 테이블 ID 목록을 입력받아 등록 
  - 주문 테이블 ID 목록이 비어있거나 2개 미만일 경우 등록할 수 없음.
  - 주문 테이블 ID 목록에 해당하는 주문 테이블이 미리 등록되어 있지 않을 경우 등록할 수 없음.
  - 주문 테이블 ID 목록에 해당하는 주문 테이블이 비어있지 않거나, 이미 단체 지정 되어 있을 경우 등록할 수 없음.
- 단체 지정을 해제할 수 있다.
  - 해제한 단체 지정 ID를 입력받아 해제 
  - 단체 지정에 해당하는 주문 테이블에 해당하는 주문의 상태가 `조리중` 또는 `식사중` 일 경우 해제할 수 없음.

## 도메인 분석
### 상품
- 상품 도메인은 `이름`, `가격`을 가진다.
- 이름이 없을 수 없음.

### 메뉴 그룹
- 메뉴 그룹 도메인은 `이름`을 가진다.
- 이름이 없을 수 없음.

### 메뉴
- 메뉴 도메인은 `이름`, `가격`을 가진다.
- `이름`이 없을 수 없음. 
- 메뉴 도메인은 `메뉴 그룹` 도메인과 N:1 관계이다.
- 메뉴 도메인은 `메뉴 상품` 도메인과 1:N 관계이다.

### 메뉴 상품
- 메뉴 상품 도메인은 `수량` 속성을 가진다.
- 메뉴 상품 도메인은 `메뉴` 도메인과 N:1 관계이다.
- 메뉴 상품 도메인은 `상품` 도메인과 N:1 관계이다.

### 주문
- 주문 도메인은 `주문 상태`, `주문 시각`을 가진다. 
- 주문 도메인은 `주문 테이블` 도메인과 N:1 관계이다.
  
### 주문 항목
- 주문 항목 도메인은 `수량` 속성을 가진다.
- 주문 항목 도메인은 `주문` 도메인과 N:1 관계이다.
- 주문 항목 도메인은 `메뉴` 도메인과 N:1 관계이다.

### 주문 테이블
- 주문 테이블 도메인은 `방문한 손님수`, `빈 테이블` 속성을 가진다.
- 주문 테이블 도메인은 `단체 지정` 도메인과 N:1 관계이다.

### 단체 지정
- 단체 지정 도메인은 `생성일시` 속성을 가진다.

## 바운더리 컨텍스트
- 상품
- 메뉴 그룹
- 메뉴
  - 메뉴 상품
- 주문 테이블
- 주문
  - 주문 항목
- 단체 지정

> 상품의 가격, 메뉴의 가격 별개의 바운더리 컨텍스트이므로 별개의 VO로 관리해야하는가?
> 정책이 다르므로 별개의 VO로 관리

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

### step1
- [X] 요구사항 정리
- [X] 테스트 코드 작성

### step2
- [X] 인수 테스트 코드 작성
- [ ] 비즈니스 서비스 리팩터링
  - [X] 메뉴 그룹
  - [X] 메뉴
  - [X] 주문
  - [X] 상품
  - [X] 단체 지정
  - [ ] 주문 테이블
- [ ] Pure Domain Model 변경
  - [ ] 메뉴 그룹
  - [ ] 메뉴
  - [ ] 주문
  - [ ] 상품
  - [X] 단체 지정
  - [ ] 주문 테이블
