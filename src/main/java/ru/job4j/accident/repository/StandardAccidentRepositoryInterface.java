package ru.job4j.accident.repository;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.Collection;

public interface StandardAccidentRepositoryInterface {
    Accident saveOrUpdateAccident(Accident accident);

    Collection<Accident> findAllAccidents();

    Accident findAccidentById(int id);

    void deleteAccidentById(int id);

    Collection<AccidentType> findAllAccidentTypes();

    AccidentType findAccidentTypeById(int id);

    Collection<Rule> findAllRules();

    Rule findRuleById(int id);
}
