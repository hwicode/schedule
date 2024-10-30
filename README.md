# <img src="https://github.com/user-attachments/assets/28098d3b-afb3-47bb-a697-6a93fcaea4c5" width="30" height="30"> schedule



## 🖐 프로젝트 소개
+ 계획표를 작성하는 것이 중요한 것임을 알지만, 계획대로 진행되지 않아서 의욕이 저하되었습니다.
+ 적절한 목표 설정과 과제 난이도 측정에 대한 고민이 있었습니다.

<br>

+ 위와 같은 문제를 해결하기 위해 프로젝트를 만들게 되었습니다.

<br><br>

## 📝 목차
+ [🖥 서비스 화면](#-서비스-화면)
+ [🏛 프로젝트 아키텍처](#-프로젝트-아키텍처)
+ [📈 프로젝트 배경](#-프로젝트-배경)
    + [📌 프로젝트 목적](#-프로젝트-목적)
    + [🔨 프로젝트 주요 기능](#-프로젝트-주요-기능)
+ [🧐 문제 해결 과정](#-문제-해결-과정)
    + [😵 점점 복잡해지는 코드](#-점점-복잡해지는-코드)
    + [🚀 성능 테스트](#-성능-테스트)
    + [✅ 로그인](#-로그인)
    + [🔨 테스트 코드](#-테스트-코드)
    + [🏛 아키텍처](#-아키텍처)
    + [🤖 프론트엔드](#-프론트엔드)
+ [📒 프로젝트 문서](#-프로젝트-문서)

 
<br><br>

## 🖥 서비스 화면
<img src="https://github.com/hwicode/schedule/assets/95541996/d058f80d-fb60-433f-a775-db6c8a4d0d96" width="650" height="500">

<br><br>

## 🏛 프로젝트 아키텍처
<img src="https://github.com/user-attachments/assets/a66f92cd-931c-45d4-92a8-1b4474d59f74" width="650" height="350">

+ 서비스 URL : https://hwi-schedule.p-e.kr/
+ 모든 요청은 https 사용

<br><br>

## 📈 프로젝트 배경

### 📌 프로젝트 목적
+ 사용자가 학습 과정을 객관적으로 측정할 수 있도록 지원합니다.
+ 측정된 결과를 통해 사용자가 학습 방식을 개선할 수 있습니다.
+ 점진적으로 사용자의 학습량과 학습 효율 증진을 높일 수 있습니다.

[👉세부 내용](https://github.com/hwicode/schedule/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EB%AA%85)

<br>

### 🔨 프로젝트 주요 기능
+ 계획표 구성 - 과제, 학습 시간, 메모, 일일 회고 작성 가능
+ 과제 관리 - 과제마다 난이도 점수와 진행 상태를 가짐
+ 달력 구성 - 목표와 일별로 (총 점수, 진행률, 메인 태그, 이모지)를 가짐
+ 태그 기능 - 계획표와 메모에 태그 추가 및 태그를 통한 검색 가능
+ 복습 기능 - 과제를 복습 주기에 맞춰 자동으로 추가 가능


<br><br>

## 🧐 문제 해결 과정

### 😵 점점 복잡해지는 코드
+ 하나의 테이블에 여러 개의 엔티티 사용
+ 비즈니스 로직을 격리를 위한 애플리케이션 레이어 설계
+ 하나의 엔티티가 논리적으로 함께 변해야 하는 엔티티들을 담당
+ 애플리케이션 레이어와 도메인 레이어의 역할 구분 및 계층 분리
+ 아쉬운 점

[👉세부 내용](https://github.com/hwicode/schedule/issues/154)

<br>

### 🚀 성능 테스트
+ 성능 테스트와 모니터링을 통해 서버가 감당 가능한 트래픽 파악
+ 서버 자원을 효율적으로 관리하기 위해 최적의 설정 도출
+ 성능 테스트 중 발생한 의문점을 해결
+ 성능 테스트 시나리오를 작성하고, 문제를 발견할 때마다 개선하면서 진행

[👉세부 내용](https://github.com/hwicode/schedule/issues/142)

<br>

### ✅ 로그인
+ OAuth 2.0을 사용한 이유
+ 세션 vs JWT 토큰
+ JWT의 보안 문제 해결 방안
+ Refresh token의 구현을 어떻게 할까?
+ Access token과 Refresh token을 프론트로 어떻게 전달 해줘야 할까?
+ Access token을 프론트에서 어디에 저장할까?
+ Refresh token에 적용되는 보안
+ 토큰 탈취당했을 때 해결 방안

[👉세부 내용](https://github.com/hwicode/schedule/issues/134)
    
<br>

### 🔨 테스트 코드
+ 테스트의 목적
+ 테스트 작성 방법
+ 테스트 작성 시 고려 사항
+ 결과
+ 아쉬운 점

[👉세부 내용](https://github.com/hwicode/schedule/issues/152)

<br>

### 🏛 아키텍처
+ 깃허브 액션을 통한 CI/CD
+ Nginx를 이용한 blue/green 무중단 배포 과정
+ 이전 APP을 삭제하는 이유

[👉CI/CD 세부 내용](https://github.com/hwicode/schedule/issues/137) <br/>
[👉무중단 배포 세부 내용](https://github.com/hwicode/schedule/issues/151)
  
<br>

### 🤖 프론트엔드
+ 서비스 화면
+ 프로젝트 시작 전 상황
+ 학습 과정
+ 주요 문제
+ 결과
+ 아쉬운 점

[👉세부 내용](https://github.com/hwicode/scheule-front)

<br><br>

## 📒 프로젝트 문서
+ [프로젝트 아쉬운 점](https://github.com/hwicode/schedule/issues/150)
+ [프로젝트 이슈 모음](https://github.com/hwicode/schedule/wiki)
+ [패키지](https://github.com/hwicode/schedule/wiki/%ED%8C%A8%ED%82%A4%EC%A7%80)
+ [프로젝트 설계 과정](https://github.com/hwicode/schedule/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EA%B3%84-%EA%B3%BC%EC%A0%95)
+ [API 문서](https://github.com/hwicode/schedule/wiki/API-%EB%AC%B8%EC%84%9C)
+ [브랜치 전략](https://github.com/hwicode/schedule/wiki/%EB%B8%8C%EB%9E%9C%EC%B9%98-%EC%A0%84%EB%9E%B5)
+ [ERD](https://github.com/hwicode/schedule/wiki/ERD)
+ [유저 스토리](https://github.com/hwicode/schedule/issues/5)
