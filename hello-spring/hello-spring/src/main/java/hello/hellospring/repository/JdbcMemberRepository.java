package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//구현을 jdbc를 사용해서 연동을 할거야
public class JdbcMemberRepository implements MemberRepository {

//   DB에 붙으려면 datasource가 필요하다. (javax sql의 datasource)
    private final DataSource dataSource;
//    spring에게 주입받아야함.
//    접속정보를 스프링이 만들어두고, 그걸 주입하게
    public JdbcMemberRepository (DataSource dataSource) {
        this.dataSource = dataSource;
// 커넥션으로 진짜 DB에 대해 열린 소켓이 열린걸 가져올 수 있다.
//        여기서 get connection 해도 되지만, 새로운 connection이 주어진다.
//        spring 프레임워크를 통해 datasource를 쓸 때에는 dataSourceUtils를 통해서 획득해야한다.
//      !!!  그래야 트랜잭션을 똑같은걸로 유지시켜준다. !!!
//        스프링 프레임워크를 쓸때에는 아래의 getCOnnection을 통해서 해야한다.
//
//        동일한 사례로 close도 DataSourceUtils를 통해서 닫아야(release)한다.
//추후 스프링 DB 접근에서 알려주겠당
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// try exception문법으로 해도 되나, 고전스타일

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
//            sql을 넣는다.
//            db에 insert 한 뒤, id값을 가져오기 위함
            pstmt.setString(1, member.getName());
            pstmt.executeUpdate();
//            Insert ... 하는 내용이 DB에 날아가는 시점
            rs = pstmt.getGeneratedKeys();
//           생성한 key를 반환해준다. (id, 1번 2번 ... n번째)
            if (rs.next()) {
                member.setId(rs.getLong(1));
//                값이 있으면 set (getLong) 해서 값을 가져오면 된다.
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
//        select 쿼리 날려서 가져오고
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
//            sql 날리고
            pstmt.setLong(1, id);
//            pstmt 세팅한 다음에

            rs = pstmt.executeQuery();
//            조회는 execute Query, DB에 직접 요청을 날린 시점
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
}
