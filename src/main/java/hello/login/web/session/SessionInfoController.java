package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return "세션이 없습니다.";
        }

        //세션 데이터 출력 (세션이 있으면 루프를 돌려서 출력)
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());                                //세션 아이디
        log.info("MaxInactiveInterval={}", session.getMaxInactiveInterval());     //세션을 비활성화 시키는 최대 간격
        log.info("CreationTime={}", new Date(session.getCreationTime()));         //세션 생성 시간
        log.info("LastAccessedTime={}", new Date(session.getLastAccessedTime())); //세션 마지막으로 접근한 시간 (사용자가)
        log.info("isNew={}", session.isNew());                                    //세션 방금 만든 것인지, 아닌지

        return "세션 출력";
    }
}
