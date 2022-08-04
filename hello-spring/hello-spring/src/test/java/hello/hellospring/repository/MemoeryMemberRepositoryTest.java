package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoeryMemberRepositoryTest {
//    MemberRepository repository = new MemoryMemberRepository();
//    메모리 리포지토리 테스트만 하니까 -> clearStore를 사용하기 위함이다.
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
//        System.out.println("result : " + (member == result));

//        Assertions.assertEquals(member, result);
        //       org.junit.jupiter.api.Assertions
//        1번인자 : 기대한 것, expected
//        2번인자 : actual
//        tru면 정상처리 아니면 에러 내보냄
//        Assertions.assertEquals(member, null);
        assertThat(member).isEqualTo(result);
//       org.junit.jupiter.api.Assertions 인줄 알았으나,  org.assertj.core.api.Assertions임
//        이걸 더 많이 쓴대
//        더 편하게 쓸 수 있다고 (assertThat 문법)
//        Add static import for 'org.assertj.core.api.Assertions.assertThat' => static import 해서 Assertions.을 없앨 수 있다.
//        import static org.assertj.core.api.Assertions.assertThat;로 올라가짐
    }

    @Test
    public void findByName() {
//given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member22 = new Member();
        member22.setName("spring2");
        repository.save(member22);
        //when
        Member result = repository.findByName("spring1").get();
        //then
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
        //when
        List<Member> result = repository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);
    }
}
