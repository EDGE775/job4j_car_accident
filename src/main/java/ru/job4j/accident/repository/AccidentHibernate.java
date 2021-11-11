package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.Collection;

public class AccidentHibernate implements StandardAccidentRepositoryInterface {
    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Accident saveOrUpdateAccident(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            if (accident.getId() == 0) {
                session.save(accident);
            } else {
                session.update(accident);
            }
            session.getTransaction().commit();
            return accident;
        }
    }

    @Override
    public Collection<Accident> findAllAccidents() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("select distinct a from Accident a "
                            + "left outer join fetch a.rules "
                            + "left outer join fetch a.type "
                            + "order by a.id", Accident.class)
                    .list();
        }
    }

    @Override
    public Accident findAccidentById(int id) {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("select distinct a from Accident a "
                            + "left outer join fetch a.rules "
                            + "left outer join fetch a.type "
                            + "where a.id = :id", Accident.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public void deleteAccidentById(int id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.delete(findAccidentById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public Collection<AccidentType> findAllAccidentTypes() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from AccidentType")
                    .list();
        }
    }

    @Override
    public AccidentType findAccidentTypeById(int id) {
        try (Session session = sf.openSession()) {
            return session.get(AccidentType.class, id);
        }
    }

    @Override
    public Collection<Rule> findAllRules() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("from Rule")
                    .list();
        }
    }

    @Override
    public Rule findRuleById(int id) {
        try (Session session = sf.openSession()) {
            return session.get(Rule.class, id);
        }
    }
}