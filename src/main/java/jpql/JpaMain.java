package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

//            member.setTeam(team);

            em.flush();
            em.clear();

            /**
             * 명시적 조인 - join 키워드를 직접 사용
             * 묵시적 조인
             * - join 키워드를 안쓰고 조인을 사용
             * - 항상 내부 조인
             * - 한눈에 파악하기 힘듬
             * - 실무에서 사용하지 말아야한다.
             */
            // 실무에서는 절대로 묵시적 내부 조인쓰지 말자.

            // 사용자 정의 함수
//            String query = "select group_concat('group_concat', m.username) from Member  m";

            // 묵시적이 내부 조인 발생, 탐색 0
//            String query = "select m.team from Member m";

            // 실패한 쿼리
//            String query = "select t.members.username from Team t";

            // 묵시적 내부 조인 발생, 탐색 X , 명시적 조인을 가지고 와서 성공
//            String query = "select m.username from Team t join t.members m";

            // N + 1 문제가 나옴.
//            String query = "select m from Member m";

            /**
             * fetch join
             * 1차캐시를 사용하지 않고 한번에 조회함.
             * 지연로딩을 해도 fetch join이 사용됨.(즉시로딩)
             * 페치 조인은 객체 그래프를 sql 한번에 조회하는 개념
             *
             */
//            String query = "select m from Member m join fetch m.team";

//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            //            System.out.println("result() = " + result);

//            for (Member member : result) {
//                System.out.println("member.getUsername() = " + member.getUsername() + ", " + member.getTeam().getName());
//                // 회원1, 팀A(sql)
//                // 회원2, 팀A(1차캐시)
//                // 회원3, 팀B(sql)
//
//                // 회원 100명 -> N + 1
//            }

            /**
             * 컬렉션 연관관계
             * 뻥튀기 문제 해결(distinct)
             * distinct
             *  - 추가로 애플리케이션에서 중복 제거시도
             *  - 같은 식별자를 가진 team 엔티티 제거
             * fetch join
             *  - as(별칭) 가급적 사용하지 말아야한다.
             *  - 둘 이상의 컬렉션은 페치 조인 할 수 없다.
             *  - 컬렉션을 페치 조인하면 페이징 api를 사용할 수 없다.
             *
             */
//            String query = "select distinct t from Team t join fetch t.members";
////            String query = "select t from Team t join fetch t.members m";
//
//            List<Team> result = em.createQuery(query, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(1)
//                    .getResultList();
//
//            System.out.println("result.size() = " + result.size());
//
//            for (Team team : result) {
//                System.out.println("team.getUsername() = " + team.getName() + ", members = " + team.getMembers().size());
//                for (Member member : team.getMembers()) {
//                    System.out.println(" ---> member = " + member);
//                }
//            }




//            String query = "select t from Team t";
//            String query = "select t from Team t join fetch t.members m";

//            List<Team> result = em.createQuery(query, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(2)
//                    .getResultList();
//
//            System.out.println("result.size() = " + result.size());
//
//            for (Team team : result) {
//                System.out.println("team.getUsername() = " + team.getName() + ", members = " + team.getMembers().size());
//                for (Member member : team.getMembers()) {
//                    System.out.println(" ---> member = " + member);
//                }
//            }


            /**
             * 엔티티 직접 사용
            */
//            String query = "select m from Member m where m = :member";
//
//            Member findMember = em.createQuery(query, Member.class)
//                    .setParameter("member", member1)
//                    .getSingleResult();
//
//            System.out.println("findMember = " + findMember);

            /**
             * 엔티티 직접 사용
             * 외래키
             */
//            String query1 = "select m from Member m where m.team = :team";
//
//            List<Member> resultList = em.createQuery(query1, Member.class)
//                    .setParameter("team", teamA)
//                    .getResultList();
//
//            for (Member member : resultList) {
//                System.out.println("member = " + member);
//            }

            /**
             * Named 쿼리 - 어노테이션
             * - 미리 정의해서 이름을 부여해두고 사용하는 jpql
             * - 정적 쿼리
             * - 어노테이션, xml에 정의 ( xml 이 우선권을 갔는다.)
             * - 애플리케이션 로딩 시점에 초기화하고 재사용
             * - 애플리케이션 로딩 시점에 쿼리를 검증 (중요!!!)
             */
//            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
//                    .setParameter("username", "회원1")
//                    .getResultList();
//            for (Member member : resultList) {
//                System.out.println("member = " + member);
//            }

            /**
             * 벌크연산
             *  - 한 쿼리로 여러 테이블의 로우 변경 ( 엔티티)
             * 벌크연산 주의
             *  - 영속성 컨텍스트를 무시하고 디비에 직접 쿼리
             *  - 벌크 연산을 먼저 실행 후 영속성 컨텍스트 초기화
             *  - 자동 em.flush() 호출 후 쿼리문 실행
             *  - 벌크 연산 실행 후 em.clear() 실행해야함.
             *  -
             *  -
             */
            int resultCount = em.createQuery("update Member m set m.age = 20 ")
                .executeUpdate();

//            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());

            System.out.println("resultCount = " + resultCount);

            System.out.println("member1.getAge() = "+ member1.getAge());
            System.out.println("member2.getAge() = "+ member2.getAge());
            System.out.println("member3.getAge() = "+ member3.getAge());

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
