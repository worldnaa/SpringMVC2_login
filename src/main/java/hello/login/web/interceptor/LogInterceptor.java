package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String logId = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, logId);

        //@RequestMapping 사용할 경우 : HandlerMethod 가 사용된다
        //정적 리소스 사용할 경우       : ResourceHttpRequestHandler 가 사용된다
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다
            // hm. 해서 다양한 정보를 호출해 볼 수 있다
            // hm.getBean(); 등등
        }

        log.info("REQUEST [{}][{}][{}]", logId, requestURI, handler);
       
        //return false; //여기서 종료
        return true;    //다음 핸들러(컨트롤러) 호출
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        //예외가 터진 경우
        if (ex != null) {
            log.error("afterCompletion error!!", ex); //에러출력
        }
    }
}
