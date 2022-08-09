package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

//    MemberService memberService = new MemberService();
//    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
////    static은 instance와 상관없이 class에 붙는거긴 하지만, 그래도 좀 그렇다.
////    memberService에서 불러온 Memory Member Repository랑
////    테스트 케이스에서의 Memory Member Repository가 다른 instance
////    class가 아니라 instance에서 한다면, 같은걸로 테스트하는게 맞는건데, 다른 instance를 사용하는 거다.
////    같은 인스턴스를 사용하기 위해서는 아래로 해야한다.
    MemberService memberService;
    MemoryMemberRepository memberRepository;
    //    동작하기 전에 넣어준다
    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
//    외부에서 넣어준다, Dependency Injection DI



    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }


    @Test
    void join() {
    }

    @Test
    void 회원가입() {
        // given 뭔가가 주어졌는데 , 이 데이터를 기반으로
        Member member = new Member();
//        member.setName("hello");
        member.setName("spring");

        // when 이걸 실행했을 때 , 여기에서 검증하는 구나
        Long saveId = memberService.join(member);

        // then 이게 나와야돼 (거의 머리 가슴 배, 테스트 기본 패턴)
        Member findMember = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }
//    테스트는 한글로 써서 직관적으로 하기도 함
//    테스트는 정상도 중요하지만, 예외 확인도 중요하다.
//    예외확인이 반쪽이다.

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
//        발생해야할 exception class, 이게 발생하는 상황은 이런 로직을 태울 때 발생한다의 이런 로직이 2번째 인자
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

//        assertThrows(NullPointerException.class, () -> memberService.join(member2));
////        null pointer로 하면 에러가 남

//        try {
//            memberService.join(member2);
//            fail("예외 발생");
//        } catch (IllegalStateException e) {
////            예외가 생겨서 정상적으로 실패가 뜬 경우
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }


        // then
    }
    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}