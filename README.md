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
    4. 공지사항 조회 - 조회수 증가
-------------------------------------------

  ## DB

### Notice 테이블

`Notice` 테이블은 공지사항에 대한 정보를 저장합니다. 각 공지사항은 여러 개의 첨부파일(`NoticeAttachment`)을 가질 수 있습니다.

#### 테이블 구조

| 컬럼 이름     | 데이터 타입     | 설명                  | 제약 조건                       |
|---------------|-----------------|-----------------------|---------------------------------|
| id            | BIGINT          | 공지사항의 고유 식별자 | PRIMARY KEY, AUTO_INCREMENT     |
| title         | VARCHAR(255)    | 공지사항의 제목        | NOT NULL                        |
| content       | TEXT            | 공지사항의 내용        |                                 |
| start_date    | TIMESTAMP       | 공지사항의 시작 날짜   |                                 |
| end_date      | TIMESTAMP       | 공지사항의 종료 날짜   |                                 |
| view_count    | INT             | 공지사항의 조회수      | DEFAULT 0                       |
| author        | VARCHAR(255)    | 공지사항 작성자의 이름 |                                 |
| created_date  | TIMESTAMP       | 공지사항이 생성된 날짜 | DEFAULT CURRENT_TIMESTAMP       |
| version       | BIGINT          | 동시성 제어를 위한 버전 |                                 |

### Notice_Attachment 테이블 

`NoticeAttachment` 테이블은 공지사항에 첨부된 파일에 대한 정보를 저장합니다. 각 첨부파일은 하나의 공지사항(`Notice`)에 속합니다.

#### 테이블 구조

| 컬럼 이름    | 데이터 타입     | 설명                   | 제약 조건                       |
|--------------|-----------------|------------------------|---------------------------------|
| id           | BIGINT          | 첨부파일의 고유 식별자  | PRIMARY KEY, AUTO_INCREMENT     |
| file_name    | VARCHAR(255)    | 첨부파일의 이름         | NOT NULL                        |
| file_type    | VARCHAR(255)    | 첨부파일의 MIME 타입    | NOT NULL                        |
| data         | LONGBLOB        | 첨부파일의 실제 데이터  | NOT NULL                        |
| notice_id    | BIGINT          | 첨부파일이 속한 공지사항 | FOREIGN KEY                     |

#####  연관관계


    Notice 테이블과 NoticeAttachment 테이블은 1대 다(One-to-Many) 관계
    NoticeAttachment 테이블의 notice_id 컬럼은 Notice 테이블의 id 컬럼을 참조하는 외래 키
    외래 키 제약 조건을 통해 Notice가 삭제되면 NoticeAttachment도 함께 삭제(ON DELETE CASCADE).
   
  ### 개발 전략
   
       1. 확인이 쉽도록 h2 내장 데이터베이스 사용.
       2. POJO 사용 
       3. 기본 JPARepository가 제공하는 Paging 활용 
       4. version 추가하여 동시성 제어
       5. 공지사항 조회시 조회수 증가
       6. 파일 업로드를 포함한 공지 저장/수정

     
---------------------------------------------------

   #### h2 console
     http://localhost:8080/h2-console, sa/sa1234

   #### open doc
     http://localhost:8080/swagger/index.html
     api 설명 및 테스트 request 파라미터 정보 표기     
  
---------------------------------------------  

  
