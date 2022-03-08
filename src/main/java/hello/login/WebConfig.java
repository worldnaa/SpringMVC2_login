package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration       //implements WebMvcConfigurer : 인터셉터 사용을 위해
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor()) //내가 만든 로그 인터셉터 생성
                .order(1)
                .addPathPatterns("/**") //하위는 전부 다 포함
                .excludePathPatterns("/css/**", "/*.ico", "/error"); //이 경로는 인터셉터 제외

        registry.addInterceptor(new LoginCheckInterceptor()) //내가 만든 로그인 인터셉터 생성
                .order(2) //순서 두번째
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                                     "/css/**", "/*.ico", "/error");
    }


    //필터를 사용하려면 스프링 빈으로 등록해야 한다
    //스프링 부트가 WAS 를 들고 띄우는데, 이렇게 등록하면 필터를 넣어서 띄워준다
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(); //필터 등록 빈 만들기
        filterRegistrationBean.setFilter(new LogFilter()); //우리가 만든 필터 넣어주기
        filterRegistrationBean.setOrder(1); //필터가 체인으로 여러개 들어갈 수 있어서 순서를 정해준다
        filterRegistrationBean.addUrlPatterns("/*"); //모든 URL에 적용

        return filterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(); //필터 등록 빈 만들기
        filterRegistrationBean.setFilter(new LoginCheckFilter()); //우리가 만든 필터 넣어주기
        filterRegistrationBean.setOrder(2); //필터가 체인으로 여러개 들어갈 수 있어서 순서를 정해준다
        filterRegistrationBean.addUrlPatterns("/*"); //모든 URL에 적용

        return filterRegistrationBean;
    }
}
