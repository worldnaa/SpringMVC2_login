package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static 사용 주의
    private static long sequence = 0L; //static 사용 주의

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);

        store.put(member.getId(), member); //Map<Long, Member> store
        return member;
    }

    public Member findById(Long id) {
        return store.get(id); //value 리턴
    }

    //Optional: Optional 이란 껍데기 통에 회원 객체가 있을 수도 있고, 없을 수도 있다
    public Optional<Member> findByLoginId(String loginId) {

//        List<Member> all = findAll();
//        for (Member m : all) {
//            if (m.getLoginId().equals(loginId)) {
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();

        //위 코드를 람다식을 이용하여 줄일 수 있다
        //findAll().stream() : findAll() 결과인 list 를 stream 으로 바꾼다 (루프를 돌리는 것과 비슷)
        //.filter() : 조건에 만족하는 애만 다음 단계로 넘어간다 (만족하지 않으면 버려진다)
        //.findFirst() : .filter() 를 제일 먼저 통과한 애만 리턴 (나머지는 무시)
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        //store에 담긴 값 중 key 빼고, value들만 list로 저장
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

}
