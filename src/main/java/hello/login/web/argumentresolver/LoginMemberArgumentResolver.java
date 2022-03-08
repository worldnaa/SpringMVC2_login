package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        log.info("supportsParameter 실행");

        //HomeController 의 homeLoginV3ArgumentResolver() 에서 파라미터에 @Login 이 있는지
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        //HomeController 의 homeLoginV3ArgumentResolver() 에서 파라미터에 @Login 이 Member 타입인지
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        //둘 다 만족하면 true => 아래 resolveArgument 실행 (false 면 실행X)
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            return null;
        }

        //세션이 있으면 Member 반환, 없으면 null 반환
        return session.getAttribute(SessionConst.LOGIN_MEMBER); 
    }
}
