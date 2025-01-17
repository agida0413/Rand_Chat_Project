 


  # 📝위치기반 랜덤채팅 

 - **기능**

   - **회원가입/로그인 등 인증,인가와 관련된 기능**
     
   - **원하는 거리조건을 설정 후 거리기반 1대1 매칭 시스템**

   - **매칭된 회원과의 채팅을 할지 말지 정하는 수락/거절**
     
   - **실시간 채팅 및 채팅 데이터 영구 저장**
      

   

     

<br>
<br>

  # 📝 유저플로우
  <img width="3937" alt="Rand_Chat User Flow" src="https://github.com/user-attachments/assets/fc420700-41d7-49dd-b4ee-308f0caa3314" />

  
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
<br>


   ✔️ DB





   

 <div>  <span><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"></span> &nbsp
    <span>   <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">  </span> &nbsp;

</div>

<br>
<br>


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

- **Spring Security & JWT 토큰을 통한 인증/인가 – Stateless 설계**
  
- **Nginx 웹 서버를 통한 리버스 프록시 및 로드 밸런싱 - > 부하분산**
  
- **기능 +  성능 중심의 개발 - > Jmeter 를 통한 부하테스트와 성능 최적화 진행**
  
- **Redis GeoLocation 을 통한 희망 거리조건에 따라  회원을 매칭하는 매칭 대기 열 구현**
  
- **Redis Pub/Sub + STOMP프로토콜 구조 위에서 분산환경에서의 SSE / WebSocket 통신 가능하도록 구현**
  
- **RDS Master/Slave 구조를 통해 쓰기,읽기 작업 분산처리**
  
- **비즈니스 별 인스턴스(인증서버, 웹 소켓 서버, 매칭 서버, 채팅I/O 서버 등)을  분리,분산하여 MSA적 성격을 띄고 있음.**
  
- ** WebClient API (WebFlux)를 통해 채팅 웹소켓서버와 채팅 I/O서버간 통신 - > NonBlocking , Async I/O 를 통해 성능개선**
  
- **Docker / GitHub Actions 를 통해 지속적 배포 및 버전 관리**   



    


<br>
<br>












#  📊 CI/CD 전략

![cd drawio](https://github.com/user-attachments/assets/6795193b-a721-4f59-ae30-61d7f39d18f9)

<br>
<br>


#  📊 전체 소프트웨어 구조

![아키 drawio](https://github.com/user-attachments/assets/66357b0f-28a4-4e93-937d-4b8d2cd5c7fd)
<br><br>



#  📊 매칭서버 아키텍처
<br><br>


![매칭서버2 drawio](https://github.com/user-attachments/assets/98587347-2bf1-4d16-90bb-8114794f42fb)
<br><br>


#  📊 채팅 웹소켓 서버 && 채팅 I/0서버 아키텍처
<br><br>

![채팅아키 drawio](https://github.com/user-attachments/assets/eca6ef4e-b741-4b80-86ea-9920df0a5941)

<br>
<br>









 # 🖥️ ERD / Redis현황




![erd](https://github.com/user-attachments/assets/bba06f7f-6ae3-40c0-a06e-cb29ae1f78d8)
<br>
<br>
![레디스 drawio](https://github.com/user-attachments/assets/eaea89f9-4abd-4ab1-809b-560b6a116821)






<br>
<br>








# ✔️ PROBLEM & SOLVE

---

### <u>**Stateless 환경에서 SSE 커넥션과 웹소켓 동작 문제**</u>

- *Stateless 환경에서는 어떤 인스턴스가 SSE 커넥션, 웹소켓 커넥션을 유지하고 있는지 알 수 없음*

  - <u>***Redis Pub/Sub을 메시지 브로커로 활용하여 모든 인스턴스에 메시지를 전파***</u>
---

### <u>**STOMP 통신 시 채팅방 목록 Refresh로 인한 서버 부담 문제**</u>

- *실시간 메시지 수신 시 채팅방 정렬, 안 읽은 메시지 업데이트 등으로 인해 백엔드 성능 최적화의 한계 발생*

  - <u>***프론트엔드에서 optimistic update를 적용하고, 5분 간격으로 React Query를 활용한 polling으로 데이터 정합성 유지***</u>
---

### <u>**회원별 채팅방 리스트 조회 API의 TPS 저하 문제**</u>

- *채팅방별 안 읽은 메시지 수와 최근 메시지를 가져오기 위해 필터링되지 않은 서브 쿼리를 LEFT JOIN하여 풀스캔 발생*

  - <u>***회원 고유 번호로 필터링하여 해당 회원이 속한 채팅방만 조회하도록 변경, LEFT JOIN 최적화***</u>
  - *TPS: 6 → 631(스케일 업 포함)*
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
