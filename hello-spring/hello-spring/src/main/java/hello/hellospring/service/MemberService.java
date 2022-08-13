package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
     *
     * 회원 가입
     */
    public long join (Member member) {
//        같은 이름이 있는 중복 회원 X
        validateDuplicateMember(member);

//        Optional이 있기때문에 가능한 일들
//        optional을 통한 메서드를 쓸 수 있다.
//        if null이면 대신, null일 가능성이 있는 변수의 경우 Optional로 감싸주고
//        그러면 if present로 해서 한다.
//        orElseGet (값이 있으면 꺼내고, 값이 없으면 orElseGet, default값을 선택해줘)



        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m-> {
//            throw new IllegalStateException("이미 존재하는 회원입니다.")
//        });
        memberRepository.findByName(member.getName())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
//    서비스는 비즈니스 처리를 위한, 로직적인 롤
//    리포지토리는 기계적으로 개발스러운 용어 (insert, ...)
    public List<Member> findMembers () {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(long memberId) {
        return memberRepository.findById(memberId);
    }
}
