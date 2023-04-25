# logback
# 학습 목적
서비스 운영을 위해서는 Log는 반드시 필요합니다. <br>
Logback 설정을 하여 운영을 하기 위한 Log를 관리하는 방법을 학습합니다.

# 개념
logback은 SlF4J(Simple Logging Facade for Java) 인터페이스를 구현하는 구현체입니다. <br>
즉, Logging Framework 라고 생각하면 됩니다.

**Appender 종류** <br>
- ConsoleAppender: 콘솔에 log를 출력
  - 개발 환경에서 자주 쓰임 
- FileAppender: 파일 단위로 log를 저장
  - 운영 환경에서 자주 쓰임 
- RollingFileAppender: log를 여러 파일로 나누어 저장
- SMTPAppender: log를 메일로 전송하여 기록
- DBAppender: log를 DB에 저장

### 준비
**logback-spring.xml**
```
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <include resource="logback-spring-${spring.profiles.active}.xml"/>
</configuration>
```

**logback-variables.properties**
- 환경 변수 설정 (로그 파일 저장 위치, 로그 출력 패턴)
```
LOG_DIR=logs
LOG_PATTERN=[%5level] %d{yyyy-MM-dd HH:mm:ss} [%thread] [%logger{40}:%line] - %msg%n
```
**logback-spring-local.xml**
- 로컬 환경에서 콘솔창에 로그 출력
```
<!--로컬 환경에서의 logback 설정 -->

<!--커스텀 로그백, 콘솔에 로그 출력-->
<included>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml"/>
    <appender name="CONSOLE2" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <layout>
            <pattern>
                ${LOG_PATTERN}
            </pattern>
        </layout>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/> <!-- Spring 에서 정의된 로그백 -->
        <appender-ref ref="CONSOLE2"/> <!-- 커스텀 로그백 -->
    </root>
</included>
```

**logback-spring-prod.xml**
- 운영 환경에서의 로그를 파일단위로 
```
<!--운영 환경에서의 logback 설정-->

<!--커스텀 로그백, 로그를 파일 단위로 저장-->
<included>
    <property resource="logback-variables.properties"/>
    <appender name="REQUEST1" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/request1.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--하루 뒤 로그파일 저장 위치 (logs/archive)-->
            <fileNamePattern>logs/archive/request1.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <!--로그파일의 최대 크기 (1KB)-->
            <maxFileSize>1KB</maxFileSize>
            <!--로그파일 최대 보관 주기 (30일)-->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>
                [REQUEST1] ${LOG_PATTERN}
            </pattern>
            <outputPatternAsHeader>true</outputPatternAsHeader>
        </encoder>
    </appender>

    <!--용량을 늘림-->
    <appender name="QUERY" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/query.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--하루 뒤 로그파일 저장 위치 (logs/archive)-->
            <fileNamePattern>logs/archive/query.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <!--로그파일의 최대 크기 (10KB)-->
            <maxFileSize>10KB</maxFileSize>
            <!--로그파일 최대 보관 주기 (30일)-->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>
                [REQUEST2] ${LOG_PATTERN}
            </pattern>
            <outputPatternAsHeader>true</outputPatternAsHeader>
        </encoder>
    </appender>

    <!--MDC-->
    <appender name="MDC" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/mdc.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--하루 단위 / 로그파일 저장 위치 (logs/archive)-->
            <fileNamePattern>logs/archive/mdc.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <!--로그파일의 최대 크기 (10KB)-->
            <maxFileSize>10KB</maxFileSize>
            <!--로그파일 최대 보관 주기 (30일)-->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>
                [MDC] %X{job}%n
            </pattern>
            <outputPatternAsHeader>true</outputPatternAsHeader>
        </encoder>
    </appender>

    <!--Error 로그만 따로 저장-->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/error.log</file>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>error</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--하루 단위 / 로그파일 저장 위치 (logs/archive)-->
            <fileNamePattern>logs/archive/error.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <!--로그파일의 최대 크기 (10KB)-->
            <maxFileSize>10KB</maxFileSize>
            <!--로그파일 최대 보관 주기 (30일)-->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>
                [Error] ${LOG_PATTERN}
            </pattern>
            <outputPatternAsHeader>true</outputPatternAsHeader>
        </encoder>
    </appender>

    <root level="INFO">
<!--        <appender-ref ref="REQUEST1"/>-->
<!--        <appender-ref ref="REQUEST2"/>-->
<!--        <appender-ref ref="MDC"/>-->
        <appender-ref ref="ERROR"/>
    </root>

    <!--독립적인 로그 파일 생성 상위 레벨에서 제외, QueryController1 참고 @Slf4j(topic="SQL_LOG1")-->
    <logger name="SQL_LOG1" level="INFO" additivity="false">
        <appender-ref ref="QUERY"/>
    </logger>
    <logger name="SQL_LOG2" level="INFO" additivity="false">
        <appender-ref ref="QUERY"/>
    </logger>
</included>
```
