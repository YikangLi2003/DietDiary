package com.github.yikangli2003.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Transaction {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("objectdb:ApplicationDatabase.odb");

    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    public void operation() throws RuntimeException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Perform operations here

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
