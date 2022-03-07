package hello.login.web;

// 이 클래스는 new 해서 생성해 사용하는 클래스가 아니라,
// loginMember 라는 상수값을 static 로 만들어 가져다 쓰기 위한 클래스이다
public class SessionConst {
    public static final String LOGIN_MEMBER = "loginMember";
}

// 방법1) 외부에서 생성 불가하도록, abstract(추상) class 로 만든다
// public abstract class SessionConst {
//     public static final String LOGIN_MEMBER = "loginMember";
// }

// 방법2) 외부에서 생성 불가하도록, interface 로 만든다
// public interface SessionConst {
//     String LOGIN_MEMBER = "loginMember";
// }
