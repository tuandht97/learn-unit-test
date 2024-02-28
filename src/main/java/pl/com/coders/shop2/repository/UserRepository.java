package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User create(User user) {
//        entityManager.merge(user);
        entityManager.persist(user);
        return user;
    }


    @Transactional
    public User findByEmail(String userEmail) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :userEmail", User.class);
        query.setParameter("userEmail", userEmail);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public User findById(Long userId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id = :userId", User.class);
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public void updateUser(Long userId, String newUsername) {
        entityManager.createQuery("UPDATE User u SET u.username = :newUsername WHERE u.id = :userId")
                .setParameter("newUsername", newUsername)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}

