package com.game.repository;

import com.game.config.MySessionFactory;
import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;

import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        this.sessionFactory = MySessionFactory.getSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {


        try (Session session = MySessionFactory.getSessionFactory().openSession()) {

            NativeQuery<Player> nativeQuery = session.createNativeQuery(
                    "SELECT * FROM player LIMIT ? OFFSET ? ", Player.class);
            nativeQuery.setParameter(1, pageSize);
            nativeQuery.setParameter(2, pageNumber  * pageSize);

            return nativeQuery.getResultList();
        }


    }

    @Override
    public int getAllCount() {
        try (Session session = MySessionFactory.getSessionFactory().openSession()) {
            Long singleResult =  session.createNamedQuery("Player.getAllCount", Long.class).getSingleResult();

            return singleResult.intValue();
        }

    }

    @Override
    public Player save(Player player) {
        try (Session session = MySessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(player);
            tx.commit();
            return player;
        }

    }

    @Override
    public Player update(Player player) {
        try (Session session = MySessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(player);
            tx.commit();
            return player;
        }

    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = MySessionFactory.getSessionFactory().openSession()) {
            session.beginTransaction();
            return Optional.ofNullable(session.find(Player.class, id));
        }

    }

    @Override
    public void delete(Player player) {
        try (Session session = MySessionFactory.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(player);
            session.getTransaction().commit();
        }

    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}