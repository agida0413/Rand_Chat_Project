 


  # 📝위치기반 랜덤채팅 

 - **기능**

   - **회원가입/로그인 등 인증,인가와 관련된 기능**
   - **원하는 거리조건을 설정 후 거리기반 1대1 매칭 시스템**
   - **매칭된 회원과의 채팅을 할지 말지 정하는 수락/거절**
   - **실시간 채팅 및 채팅 데이터 영구 저장**
      

   

     

<br>
<br>



  # 📝 핵심 화면 요약 



<br>
<br>












 # 🔧 Skills

   ✔️ Back-end
 
   
 <div>
       <span><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"></span> &nbsp
       <span><img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens"></span> &nbsp
       <span> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"></span>&nbsp
       <span> <img src="https://img.shields.io/badge/MyBatis-DC382D?style=for-the-badge&logo=mybatis&logoColor=white"></span>   
         <span><img src="https://img.shields.io/badge/WEBSOCKET-black?style=for-the-badge&logo=rocket&logoColor=#D33847"></span>
    <span>
  <img src="https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
</span> &nbsp;
  <span>
  <img src="https://img.shields.io/badge/Redis%20Pub%2FSub-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
</span> &nbsp;
 </div>


 <br>
    ✔️ DEV-OPS
 
   
 <div>
   <span>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=Amazon%20RDS&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/AWS%20ElastiCache-0052CC?style=for-the-badge&logo=Amazon%20AWS&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=Nginx&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/AWS%20VPC-232F3E?style=for-the-badge&logo=Amazon%20AWS&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=GitHub%20Actions&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
</span> &nbsp;
 </div>   
<br>
   ✔️ DB


   

 <div>  <span><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"></span> &nbsp
    <span>   <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">  </span> &nbsp;

</div>



   ✔️ Front-end


   
<div>  <span>
  <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/Zustand-FFC107?style=for-the-badge&logo=Zustand&logoColor=black">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/SASS-CC6699?style=for-the-badge&logo=SASS&logoColor=white">
</span> &nbsp;
<span>
  <img src="https://img.shields.io/badge/React%20Query-FF4154?style=for-the-badge&logo=ReactQuery&logoColor=white">
</span> &nbsp;

 </div>   
     
<br>



   



<br>
<br>













  # 📝 비즈니스 로직 외 중점 사항 

 - **JWT 토큰을 이용한 인증 / 인가** 

  - **스케줄러 배치 처리를 통한 토큰, 이메일 인증관리**

   - **한국어 초, 중 ,종성 분리를 통한 자동완성 기능 제공** 

  - **서비스 레벨에서의 캐싱을 통한 자동완성 기능 제공** 

   - **중복요청 방지 인터셉터** 

   - **S3를 통한 이미지 파일 관리** 

    


<br>
<br>















#  📊 소프트웨어 구조

![3 drawio](https://github.com/user-attachments/assets/d1f83e04-9fa6-4cf7-a94a-b4059f4e32c1)






<br>
<br>









 # 🖥️ ERD


![ERD](https://github.com/user-attachments/assets/8191d2ab-1412-49a1-a07d-33acf142b9aa)








<br>
<br>








# ✔️ PROBLEM & SOLVE

### <u>**Security Filter 내 예외 발생 시 공통 예외 처리 불가한 문제**</u> 

- *Filter는  Servlet 앞단에 위치하여 ControllerAdvice에 접근 불가* 

  - <u>***ResponseApi 클래스에 필요한 예외처리 항목에 대하여 response를 직접 보내주는 메소드를 작성하여 filter 내부에서 예외처리***</u>
---

### <u>**검색어 추천 기능 중 한글 초,중,종성으로 검색 불가 문제**</u> 

- *한글은 유니코드 체계상 초,중,종성 분리검색 필요* 

  - <u>***데이터베이스 내 초,종,종성컬럼을 추가하여 별도로 저장 및 , 검색컬럼 구성***</u> 
---

### <u>**동일 유저가 중복요청시 데이터 일관성 깨짐 문제**</u> 

- *같은 사용자에 대해 동일한 api요청시 막는 로직이 필요* 

  - <u>***인터셉터, ConcurrentHashMap을 이용하여 동일한 유저에대한 동일한 요청API 발생시 리터해주는 로직 추가***</u>
---

### <u>**게시물 C,U 시 데이터베이스 트랜잭션에서 예외가 발생, API요청이 실패 하여도,S3파일 객체는 저장되는 문제**</u> 

- *게시물 C, U작업 실패 시 S3파일 객체도 롤백되어 삭제 해야하는 과정이 필요* 

  - <u>***S3업로드 로직을 먼저 실행하고 데이터베이스 트랜잭션 수행 후 예외 발생 시 생성된 URL과 매치되는 S3객체 삭제***</u>
---

















<br>
<br>




# 📝 개선방안 및 추후학습



- *자동추천 검색, jwt 토큰관리 관련 작업은 RDBMS에 직접접근하는 방식이 성능저하를 일으킴* 

  - <u>***REDIS 학습을 통해 메모리(RAM)을 활용하여 캐싱하자***</u> 

<br>


- *배포를 할때 수동으로 하니 상당한 시간 및 관리가 어려움* 

  - <u>***CI/CD 학습을 통해 자동화 전략을 이용하자***</u> 

<br>

- *JWT 토큰의 STATELESS의 장점를 느끼지 못하였음* 

  - <u>***로드밸런싱을 학습하여 STATLESS의 장점을 느껴보자***</u>
    



API 명세서 - https://rand-chat.gitbook.io/rand_chat-docs
