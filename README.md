공지사항 API
programed By Hyeran 
==================================
## Spec
- Spring boot 3.1.2
- Spring data Jpa
- lombok
- H2-database
- JUnit5
- OpenJDK 17
--------------------------------------------
## build Method

     git clone https://github.com/bernakwon/notice.git

#### Run
    - NoticeApplication.main() Run
    - 포트 설정을 8080으로 해두었기 때문에 localhost:8080으로 접속해야한다.

--------------------------------------------
## 요구사항과 개발전략

 ### 요구사항 
    1. 공지사항 등록(첨부 여러개)
    2. 공지사항 수정(첨부 여러개)
    3. 공지사항 삭제
    4. 공지사항 조회
-------------------------------------------

  #### DB
    1.
    2.
   
  #### 개발 전략
   
     1. 확인이 쉽도록 h2 내장 데이터베이스 사용.
     2. POJO 사용 
     3. 기본 JPARepository가 제공하는 Paging 활용 
     4. version 추가하여 동시성 제어
     5. 

     
---------------------------------------------------

   #### h2 console
     http://localhost:8080/h2-console, sa/sa1234

   #### open doc
     http://localhost:8080/swagger/index.html
     api 설명 및 테스트 request 파라미터 정보 표기     
  
---------------------------------------------  

  #### 실행 순서

