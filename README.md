# 동행복권 사이트 MSA 결제파트  

## 📃 프로젝트 소개
동행복권 사이트를 모티브로 복권, 연금 복권, 토토, 결제를 구현한 사이트에서 "결제"파트와 "당첨결과"서버 를 담당하였습니다.

### Win Result Server
https://github.com/encore-full-stack-5/NODAJI_WIN_RESULT

### Front-End
https://github.com/encore-full-stack-5/DH_lottery

## ⚙️ 기술스택

### Server Framework
![Spring-Boot](https://img.shields.io/badge/spring--boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-FF4438.svg?style=for-the-badge&logo=redis&logoColor=white)

### Infra Framework
![Google Cloud](https://img.shields.io/badge/GoogleCloud-%234285F4.svg?style=for-the-badge&logo=google-cloud&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/jenkins-red.svg?style=for-the-badge&logo=jenkins&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)

### Communication
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![Notion](https://img.shields.io/badge/notion-white.svg?style=for-the-badge&logo=notion&logoColor=000000)

## 프로젝트 구조
📦main<br>
 ┣ 📂generated<br>
 ┣ 📂java<br>
 ┃ ┗ 📂com<br>
 ┃ ┃ ┗ 📂nodaji<br>
 ┃ ┃ ┃ ┗ 📂payment<br>
 ┃ ┃ ┃ ┃ ┣ 📂aop<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AopForTransaction.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📜DistributedLockAop.java<br>
 ┃ ┃ ┃ ┃ ┣ 📂controller<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountController.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExceptionController.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentController.java<br>
 ┃ ┃ ┃ ┃ ┣ 📂dto<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📂request<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BuyRequestDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DepositRequestDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WithdrawRequestDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📂response<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BuyResponseDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentErrorResponseDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentSuccessResponseDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PointResponseDto.java<br>
 ┃ ┃ ┃ ┃ ┣ 📂global<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomSecurityConfig.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedissonConfig.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📂domain<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaAccountDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaBalanceDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WinDepositDto.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Account.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜History.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentHistory.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountExistException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountNotFoundException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BalanceNotEnoughException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BalanceNotZeroException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExceedsBalanceException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserNotFoundException.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repository<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountRepository.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HistoryRepository.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentHistoryRepository.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📂kafka<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountProducer.java<br>
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaStatus.java<br>
 ┃ ┃ ┃ ┃ ┣ 📂service<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountService.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AccountServiceImpl.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜HistoryService.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜HistoryServiceImpl.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentService.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentServiceImpl.java<br>
 ┃ ┃ ┃ ┃ ┣ 📂utils<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomSpringELParser.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DistributedLock.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java<br>
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtUtil.java<br>
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentUtils.java<br>
 ┃ ┃ ┃ ┃ ┗ 📜PaymentApplication.java<br>
 ┗ 📂resources<br>
 ┃ ┣ 📂static<br>
 ┃ ┣ 📂templates<br>
 ┃ ┣ 📜application-dev.yml<br>
 ┃ ┣ 📜application-local.yml<br>
 ┃ ┣ 📜application-test.yml<br>
 ┃ ┗ 📜application.yml<br>


## 🕸 ERD
![image](https://github.com/hyun1999/html-study/assets/74495717/36052596-b6fa-4de3-89d1-d8d0eff1cc9d)

## 아키텍처
![payment아키](https://github.com/hyun1999/ci-backend/assets/74495717/a5af4bfe-73bd-40fe-a436-a7abdd535b3a)

## 📄 UML

### 결제 시퀀스 다이어그램
![결제 drawio](https://github.com/hyun1999/html-study/assets/74495717/c9bc3446-55db-434f-9600-3bd5cefd0236)

### 당첨금 지급 시퀀스 다이어그램
![feign drawio](https://github.com/hyun1999/html-study/assets/74495717/05f39d8a-6c17-4ce0-b171-20b696145570)


## 🖥️ 화면 구성도

### 결제 페이지
![토스결제](https://github.com/hyun1999/html-study/assets/74495717/21e6fa01-8af4-4c39-87bc-05d5344f6762)
### 출금 페이지
![출금](https://github.com/hyun1999/html-study/assets/74495717/d109bdf5-3e06-43db-a680-7007f5587f59)
### 입출금 내역 페이지
![입출금내역](https://github.com/hyun1999/html-study/assets/74495717/f5d0d44b-16d2-4bf6-b177-20709aa62fd4)


## 느낀점

