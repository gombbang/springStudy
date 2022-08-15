package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository (EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {

////        만약 직접 코드에 공통 관심 사항을 넣어야한다면..
//        Long start = System.currentTimeMillis();
//        try {
        em.persist(member);
        return member;
//        } finally {
//        Long finish = System.currentTimeMillis();
//        Long timeMs = finish - start;
//        System.out.println("걸린 소요 시간 : " + timeMs + " ms");
//    }
    }

    @Override
    public Optional<Member> findById(Long id) {
////        만약 직접 코드에 공통 관심 사항을 넣어야한다면..
//        Long start = System.currentTimeMillis();
//        try {
            Member member = em.find(Member.class, id);
            return Optional.ofNullable(member);
//        } finally {
//            Long finish = System.currentTimeMillis();
//            Long timeMs = finish - start;
//            System.out.println("걸린 소요 시간 : " + timeMs + " ms");
//        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }
}
