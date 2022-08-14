package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//@Service
//config에서 이미 해주므로  주석처리하지 않으면 에러가 난다.
@Transactional
//jpa를 쓰면서 필요하게 된 부분
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService (MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
//        Optional이 있기때문에 가능한 일들
//        optional을 통한 메서드를 쓸 수 있다.
//        if null이면 대신, null일 가능성이 있는 변수의 경우 Optional로 감싸주고
//        그러면 if present로 해서 한다.
//        orElseGet (값이 있으면 꺼내고, 값이 없으면 orElseGet, default값을 선택해줘)

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    /**
     * 전체 회원 조회
     */
//    서비스는 비즈니스 처리를 위한, 로직적인 롤
//    리포지토리는 기계적으로 개발스러운 용어 (insert, ...)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
