package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

                    log.info("미인증 사용자 요청 {}", requestURI);

                    //로그인으로 redirect (로그인 성공 후 요청했던 페이지로 이동)
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return;
                }
            }

            chain.doFilter(request, response); //whitelist 면 chain.doFilter 바로 실행

        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
        } finally {
            log.info("인증 체크 필터 종료 {} ", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        //스프링이 제공하는 기능으로, whitelist 와 requestURI 가 단순하게 패턴이 매칭 되는가 확인
        //해당 메서드는 로그인 체크를 해야하는지 확인하는 메서드이므로, !(부정) 을 사용한다
        //whitelist 에 포함이 안 된 경로(로그인 체크를 해야하는) 들은 true 반환
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}
