<p align="center">
  <img src="https://github.com/miniato2/Ketchup_Back/blob/803ef3e86e1ca1c6cd27814d53b57b6d20590cb7/img/logo.png" width="500">
</p>

<br>

## 프로젝트 소개

Ketchup은 조직 내에서 효율적인 커뮤니케이션과 협업을 지원하는 그룹웨어 프로그램 입니다.  
조직 구성원들이 하나의 플랫폼에서 정보 공유, 일정 관리, 파일 공유, 메신저, 전자 결재 등을 수행할 수 있습니다. 이 프로그램은 업무 효율을 극대화하고, 의사소통의 투명성을 확보하는 데 목적이 있습니다.

<br>

## 팀원 소개
| 최승욱 | 이후영 | 이진우 | 김현지 | 박다은 |
|--------|--------|--------|--------|--------|
|[<img src="https://img.shields.io/badge/Github-Link-ffffff?logo=Github">](https://github.com/miniato2) | [<img src="https://img.shields.io/badge/Github-Link-ffffff?logo=Github">](https://github.com/2eehy) | [<img src="https://img.shields.io/badge/Github-Link-ffffff?logo=Github">](https://github.com/JayLee-98) | [<img src="https://img.shields.io/badge/Github-Link-ffffff?logo=Github">](https://github.com/KIMHYEONJI13) | [<img src="https://img.shields.io/badge/Github-Link-ffffff?logo=Github">](https://github.com/daeun100299) |  

<br>
  
## 개발 기간
<img src="https://github.com/miniato2/Ketchup_Back/blob/80b8255b0df9a7106380a1b6ee7a5ecc82721b5e/img/schedule.png" width="100%">
<br><br><br>

## 개발 환경

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/JPA-A5915F.svg?&style=for-the-badge&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<br>
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">

<br>

## DB 설계
<div style="display: flex; flex-wrap: nowrap; gap: 10px;">
  <img src="https://github.com/miniato2/Ketchup_Back/blob/a175d65ee29360d9d0773884c3fe706523533f43/img/erd.png" alt="ERD" style="width: 50%;" />
  <img src="https://github.com/miniato2/Ketchup_Back/blob/a175d65ee29360d9d0773884c3fe706523533f43/img/entity.png" alt="Entity" style="width: 49%;" />
</div>

<br><br><br>

## API 명세
<details>
  <summary>보기</summary>
  <div style="display: flex; flex-wrap: nowrap; gap: 10px;">
    <img src="https://github.com/miniato2/Ketchup_Back/blob/a175d65ee29360d9d0773884c3fe706523533f43/img/rest1.png" alt="REST API 1" style="width: 32%;" />
    <img src="https://github.com/miniato2/Ketchup_Back/blob/a175d65ee29360d9d0773884c3fe706523533f43/img/rest2.png" alt="REST API 2" style="width: 32%;" />
    <img src="https://github.com/miniato2/Ketchup_Back/blob/a175d65ee29360d9d0773884c3fe706523533f43/img/rest3.png" alt="REST API 3" style="width: 32%;" />
  </div>
</details>  
<br><br>

## 담당기능
<details>
  <summary>스크린샷</summary>
  <img src="https://github.com/miniato2/Ketchup_Back/blob/d5c23667cd2f4a145004901166b15d3845e32e0b/img/screenshot.png">
</details>
<br>

* 전자결재
  
  - 전자결재 백엔드 개발
    
    - 기안 상신과 함께 파일을 업로드시 에러가 발생하면 try-catch를 사용하여 트랜잭션 내에서 업로드된 파일을 모두 삭제하고 @Transactional 어노테이션을 사용하여 트랜잭션을 롤백 처리
    - 기안의 상태와 순서, 본인이 결재자나 참조자일 경우를 고려해야 하므로 복잡한 쿼리를 처리하기 위해 JPQL을 사용
    - 목록 조회 시 연관 엔티티를 한번 더 조회하는 N+1 문제를 해결하기 위해 fetch join을 사용
      
  - 전자결재 화면 구현
    
    - 상태관리를 위한 redux 라이브러리 사용, redux-thunk 미들웨어을 사용해 비동기 액션을 처리 할 수 있도록 구현
    - Quill을 사용한 기안 작성 및 양식 불러오기

<br><br>

## 시연영상

https://github.com/user-attachments/assets/dd98286d-578b-4e66-ac2b-f8fbad34457d




프론트  
https://github.com/miniato2/Ketchup_front


