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

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

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
            String query = "select m.username from Team t join t.members m";


            List<Collection> result = em.createQuery(query, Collection.class)
                    .getResultList();
            System.out.println("result() = " + result);


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
