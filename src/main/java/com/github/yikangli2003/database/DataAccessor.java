package com.github.yikangli2003.database;

import com.github.yikangli2003.database.entity.*;
import com.github.yikangli2003.database.exception.*;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.RollbackException;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataAccessor {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("objectdb:ApplicationDatabase.odb");

    public static void closeEntityManagerFactory() { entityManagerFactory.close(); }

    private static void executeInTransaction(Consumer<EntityManager> operation) {
        executeInTransaction(entityManager -> {
            operation.accept(entityManager);
            return null;
        });
    }

    private static <T> T executeInTransaction(Function<EntityManager, T> operation) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try (entityManager) {
            transaction.begin();
            T result = operation.apply(entityManager);
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // User entity related methods performing CURD operations.

    public static void signUpNewUser (
            String account,
            String hashedPassword,
            String name,
            LocalDateTime localRegistrationTime
    ) throws DuplicatedEntityPropertyException {
        try {
            executeInTransaction(entityManager -> {
                User newUser = new User(account, hashedPassword, name, localRegistrationTime);
                entityManager.persist(newUser);
            });
        } catch (RollbackException e) {
            throw new DuplicatedEntityPropertyException("User", "account", account, e);
        }
    }

    public static User getUserByAccount(String account) throws EntityNotFoundException {
        return executeInTransaction(entityManager -> {
            String jpql = "SELECT u FROM User u WHERE u.account = :account";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("account", account);

            if (query.getResultList().isEmpty()) {
                throw new EntityNotFoundException("User", "account", account);
            } else {
                return query.getResultList().getFirst();
            }
        });
    }

    public static void saveUserPropertyChange(User updatedUser) throws EntityNotFoundException {
        executeInTransaction(entityManager -> {
            if (updatedUser.getId() == null || entityManager.find(User.class, updatedUser.getId()) == null) {
                throw new EntityNotFoundException("User", "id", String.valueOf(updatedUser.getId()));
            }
            entityManager.merge(updatedUser);
        });
    }
}
