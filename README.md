# schedule

## 프로젝트 소개
+ 계획표를 작성하는 것이 중요한 것임을 알지만 계획대로 진행되지 않아서 의욕이 저하되었습니다.
+ 적절한 목표 설정은 어떻게 하는지 의문이였습니다.
+ 적절한 난이도를 어떻게 측정할지 고민이였습니다.

<br>

+ 위와 같은 문제를 해결하기 위해 프로젝트를 만들게 되었습니다.

<br><br>

## 서비스 화면
<img src="https://github.com/hwicode/schedule/assets/95541996/d058f80d-fb60-433f-a775-db6c8a4d0d96" width="650" height="500">

<br><br>

## 아키텍처
<img src="https://github.com/user-attachments/assets/a66f92cd-931c-45d4-92a8-1b4474d59f74" width="650" height="350">

+ 서비스 URL : https://hwi-schedule.p-e.kr/
+ 모든 요청은 https를 사용해야 합니다. (http 요청은 에러 발생)

<br><br>

## 프로젝트 배경

### 프로젝트 목적
사용자가 학습 과정을 객관적으로 측정하고 문제점을 개선하여, 점진적으로 학습량과 학습 효율을 높이는 것입니다.

[세부 내용](https://github.com/hwicode/schedule/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EB%AA%85)

<br>

### 프로젝트 주요 기능
+ 계획표에는 과제, 학습 시간, 메모, 일일 회고가 있습니다.
+ 과제는 난이도 점수와 진행 상태를 가집니다.
+ 달력에서 일별로 총 점수, 총 진행 상태, 메인 태그를 한눈에 볼 수 있습니다.
+ 계획표와 메모에 태그를 추가할 수 있습니다.
+ 태그로 계획표나 메모를 검색할 수 있습니다.
+ 과제를 복습 주기에 따라 자동으로 추가할 수 있습니다.

<br><br>

## 문제 해결 과정

### 점점 복잡해지는 코드
+ 하나의 테이블에 여러 개의 엔티티 사용
+ 애플리케이션 레이어에서 비즈니스 로직 격리
+ 하나의 엔티티가 논리적으로 함께 변해야 하는 엔티티들을 담당
+ 서비스 레이어와 도메인 레이어의 역할 구분
+ 아쉬운 점

<br>

+ [세부 내용](https://github.com/hwicode/schedule/issues/154)

<br>

### 성능 테스트
서버를 효율적으로 관리하기 위해 최적의 설정을 찾으며, 감당할 수 있는 부하를 객관적으로 파악하는 과정입니다.<br>
성능 테스트 진행 중 생긴 의문점 해소를 최우선으로 생각했습니다.

<br>

+ 성능 테스트 목적
+ 첫 번째 시나라오
+ 두 번째 시나리오
+ 세 번째 시나리오
+ 네 번째 시나리오
+ 부록

<br>

+ [세부 내용](https://github.com/hwicode/schedule/issues/142)

<br>

### 로그인
+ OAuth 2.0을 사용한 이유
+ 세션 vs JWT 토큰
+ JWT의 보안 문제 해결 방안
+ Refresh token의 구현을 어떻게 할까?
+ Access token과 Refresh token을 프론트로 어떻게 전달 해줘야 할까?
+ Access token을 프론트에서 어디에 저장할까?
+ Refresh token에 적용되는 보안
+ 토큰 탈취당했을 때 해결 방안

<br>

+ [세부 내용](https://github.com/hwicode/schedule/issues/134)
    
<br>

### 테스트 코드
+ 테스트의 목적
+ 테스트 작성 방법
+ 테스트 작성 시 고려 사항
+ 결과
+ 아쉬운 점

<br>

+ [세부 내용](https://github.com/hwicode/schedule/issues/152)

<br>

### 아키텍처
+ 깃허브 액션을 통한 CI/CD
+ Nginx를 이용한 blue/green 무중단 배포 과정
+ 이전 APP을 삭제하는 이유

<br>

+ [CI/CD 세부 내용](https://github.com/hwicode/schedule/issues/137)
+ [무중단 배포 세부 내용](https://github.com/hwicode/schedule/issues/151)
  
<br>

### 프론트엔드
+ 서비스 화면
+ 프로젝트 시작 전 상황
+ 학습 과정
+ 주요 문제
+ 결과
+ 아쉬운 점

<br>

+ [세부 내용](https://github.com/hwicode/scheule-front)

<br><br>

## 프로젝트 문서
+ 프로젝트 아쉬운 점
+ API 문서
+ 브랜치 전략
+ ERD
+ 패키지
+ 유저 스토리
+ 프로젝트 설계 과정
