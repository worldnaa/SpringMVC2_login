package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        //MockHttpServletResponse : 스프링이 테스트를 위해 제공하는 가짜 Response
        //MockHttpServletRequest  : 스프링이 테스트를 위해 제공하는 가짜 Request

        //세션 생성
        //서버에선 세션을 만들어 response 에 담고, 웹 브라우저로 응답이 나갔다고 가정
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장
        //여기부터 웹 브라우저 요청이라고 가정. 브라우저가 응답쿠키로 요청쿠키를 만들어 서버에 전달
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //ex) mySessionId=1231-2154-1d35-adfd 식으로 담겨있다

        //세션 조회
        //웹 브라우저에서 넘어온 세션을 서버에서 확인
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}
