# logback
# 학습 목적
서비스 운영을 위해서는 Log는 반드시 필요합니다. <br>
Logback 설정을 하여 운영을 하기 위한 Log를 관리하는 방법을 학습합니다.

# 개념
logback은 SlF4J(Simple Logging Facade for Java) 인터페이스를 구현하는 구현체입다. <br>
즉, Logging Framework 라고 생각하면 됩니다.

**Appender 종류** <br>
- ConsoleAppender: 콘솔에 log를 출력
  - 개발 환경에서 자주 쓰임 
- FileAppender: 파일 단위로 log를 저장
  - 운영 환경에서 자주 쓰임 
- RollingFileAppender: log를 여러 파일로 나누어 저장
- SMTPAppender: log를 메일로 전송하여 기록
- DBAppender: log를 DB에 저장
