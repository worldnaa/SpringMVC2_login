package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId"; //Ctrl+Alt+C : 상수로 만들기

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>(); //동시성 문제가 있을 경우 사용

    /**
     * 세션 생성
     * 1. sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * 2. 세션 저장소에 sessionId와 보관할 값 저장
     * 3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value); //value = 멤버객체

        //쿠키 생성 (Key => SESSION_COOKIE_NAME = "mySessionId" / Value => sessionId = UUID)
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId); 
        //ex) Cookie("mySessionId", "fsdfds-afdfsfsd-afdfdsf-safdfds") 이런식으로 저장 됨
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); //SESSION_COOKIE_NAME = "mySessionId"
        //ex) sessionCookie = Cookie("mySessionId", "fsdfds-afdfsfsd-afdfdsf-safdfds")

        if (sessionCookie == null) {
            return null;
        }

        //ex) sessionCookie.getValue() = "fsdfds-afdfsfsd-afdfdsf-safdfds"
        //sessionStore.get() 에서 UUID 값을 넘기면, 진짜 데이터인 Value(멤버객체) 반환
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        // 쿠키가 없으면 null 반환
        if (request.getCookies() == null) {
            return null;
        }
        
        // 쿠키가 있으면 아래 로직 실행 후 쿠키 반환
        return Arrays.stream(request.getCookies()) //Arrays.stream : 배열을 스트림으로 바꿔준다
                .filter(cookie -> cookie.getName().equals(cookieName)) //cookieName = mySessionId
                .findAny()              //.findFirst() : 순서 중에서 먼저 나온 애 1개를 반환
                .orElse(null);    //.findAny() : 순서 상관없이 빨리 나온 애 1개를 반환

        //ex) Cookie("mySessionId", "fsdfds-afdfsfsd-afdfdsf-safdfds") 이런식으로 반환
    }
}

/*
[ getSession() 리팩터링 전 ]
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
        return null;
    }
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
            return sessionStore.get(cookie.getValue());
        }
    }

[ getSession() 리팩터링 후 ]
    findCookie() 메서드 따로 만들기
*/
