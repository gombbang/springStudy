package hello.hellospring.config;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {


//    datasource는 스프링이 제공해준다.
//    @Configuration도 스프링 빈으로 관리되기 때문에, 스프링부트가 설정파일을 보고 자체적으로 bean, dataSource를 만들어준다.
//    이걸 Autowired로 주입을 해주는 형태
//    private DataSource dataSource;
//
//    @Autowired
//    public SpringConfig (DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//    JPA에서는 필요 없다오오오
//    @PersistenceContext, 스펙상 베이스이나 굳이 할 필요는 없다고, @Autowired로 대체가능하다 함
    EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }


    @Bean
    public MemberService memberService () {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository () {

//        return new MemoryMemberRepository();

//        return new JdbcMemberRepository(dataSource);
//        jdbc class 생성 및 인터페이스 확장
//        configuration만 수정했다.
//        그렇게 했음에도 가리킨 DB를 수정할 수 있다.

//        return new JdbcTemplateMemberRepository(dataSource);

        return new JpaMemberRepository(em);
    }

    @Bean
    public TimeTraceAop timeTraceAop () {
        return new TimeTraceAop();
    }
}
