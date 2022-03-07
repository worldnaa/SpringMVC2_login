package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 이면 로그인 실패
     */
    public Member login(String loginId, String password) {
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = findMemberOptional.get(); //.get() : Optional 안에 있는 걸 꺼낸다 (없으면 예외)
//        if (member.getPassword().equals(password)) {
//            return member;
//        } else {
//            return null;
//        }

        //위 코드를 아래처럼 줄일 수 있다
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        return findMemberOptional.filter(m -> m.getPassword().equals(password))
//                    .orElse(null);

        //위 코드를 아래처럼 합칠 수 있다 (Ctrl+Alt+N)
        return memberRepository.findByLoginId(loginId)
                    .filter(m -> m.getPassword().equals(password)) //Optional 안에 들어있는 멤버가 password 와 같은지
                    .orElse(null);                            //같으면 그 멤버를 반환, 다르면 null 을 반환

    }
}
