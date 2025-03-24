# 한터 글로벌 코딩 테스트 과제
<hr/>


# 문제 1

## 🔍 문제 분석

요구사항에 따르면, 다음과 같은 기능과 조건을 만족해야 합니다.

- 하위 카테고리 관리:
  - 특정 카테고리를 조회하면 하위 카테고리들도 함께 조회되어야 함.

- JSON 변환 지원:

  - 자료구조를 JSON 형식으로 변환하여 응답할 수 있어야 함.

<hr/>

## 🗂️ 자료구조 선택

### 트리 구조를 선택한 이유

1. 계층 구조 표현 <br/>
카테고리 간의 부모-자식 관계를 직관적으로 표현하기 위해 트리 구조를 선택하였습니다.

2. 효율적인 탐색<br/>
특정 카테고리부터 시작하여 재귀적으로 자식 노드를 탐색하면 손쉽게 하위 카테고리들을 조회할 수 있습니다.

<hr/>

##  자료구조

Board

- 하나의 게시판(Board)은 특정 최하위 카테고리에 속합니다.
  - 카테고리 조회 시 해당 카테고리의 하위 카테고리까지 모두 조회할 수 있기에,
  게시판과 카테고리의 관계를 최하위 카테고리로 설정하였습니다.
- 익명 게시판은 단일 인스턴스로 관리하여 중복 생성을 방지합니다.

Category

- 식별자(id)와 이름(name), 게시판(board)과의 관계를 가지고 있습니다.

- 부모 식별자(parentId)를 통해 트리 구조를 형성합니다.

- 자식 카테고리 리스트(children)를 가지고 있어 하위 카테고리를 저장합니다.

CategoryGraph

- 트리 구조를 활용한 카테고리 조회
  - 카테고리는 부모-자식 관계를 가지며, 이러한 관계를 트리 형태로 구성하여 상위 카테고리부터 하위 카테고리까지 계층적으로 탐색하도록 구현하였씁니다.

- 캐싱 처리
  - 트리 구조를 조회할 때마다 구조를 재생성 하게 되면 성능 저하가 발생하기 때문에 ConcurrentHashMap을 사용하여 캐싱을 적용하였습니다.

- 익명 게시판 처리
  - 익명 게시판을 조회하면 해당 카테고리의 익명 게시판 정보를 반환하여  사용자가 어느 카테고리에서 접근하든 동일한 익명 게시판을 확인할 수 있습니다.

<hr/>

### Test Case
<img width="459" alt="스크린샷 2025-03-23 오전 1 31 39" src="https://github.com/user-attachments/assets/8b9a0559-7f10-4367-aba4-e316a3417c8a" /> <br />

### Test Result
 <br />
<img width="284" alt="스크린샷 2025-03-23 오전 2 07 32" src="https://github.com/user-attachments/assets/d5702db6-d156-43e7-9d1a-ae46ab4fb39f" /> <br />
<img width="276" alt="스크린샷 2025-03-23 오전 2 07 56" src="https://github.com/user-attachments/assets/d4d2e5ae-60af-476e-864e-eff2f2bf3cfd" /> <br />

<hr/>


# 문제 2

서로 다른 종류의 통화를 나타내는 N 크기의 coin[ ] 정수 배열과 정수 합계가 주어지면, <br />coin[]의 다양한 조합을 사용하여 합계를 만드는 방법의 수를 찾는 것이 과제

