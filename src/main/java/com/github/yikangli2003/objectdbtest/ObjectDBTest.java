package com.github.yikangli2003.objectdbtest;

import jakarta.persistence.*;
import java.util.List;

public class ObjectDBTest {
    public static void main(String[] args) {
        // Create an EntityManagerFactory for the ObjectDB database
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:test.odb");
        EntityManager em = emf.createEntityManager();

        // Create a new entity transaction
        EntityTransaction tx = em.getTransaction();

        try {
            // Begin the transaction
            tx.begin();

            // Create and persist a new entity
            MyEntity entity = new MyEntity();
            entity.setName("Solomon Reed");
            em.persist(entity);

            // Commit the transaction
            tx.commit();
        } catch (Exception e) {
            // Rollback the transaction in case of an error
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            // Show all entities
            showAllEntities(em);

            // Close the EntityManager
            em.close();
            emf.close();
        }
    }

    private static void showAllEntities(EntityManager em) {
        List<MyEntity> entities = em.createQuery("SELECT e FROM MyEntity e", MyEntity.class).getResultList();
        for (MyEntity entity : entities) {
            System.out.println("ID: " + entity.getId() + ", Name: " + entity.getName());
        }
    }
}