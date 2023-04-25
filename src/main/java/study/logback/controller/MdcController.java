package study.logback.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MdcController {
    /*
    멀티 쓰레드 환경에서 로그를 남길 때 사용하는 개념
     */

    @GetMapping("/mdc")
    public String mdc() {
        MDC.put("job", "dev");
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        MDC.clear();
        return "mdc";
    }
}