package ru.job4j.accident.service;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.List;
import java.util.Optional;

public interface AccidentService {
    Accident saveOrUpdateAccident(Accident accident, String[] ids);

    Optional<Accident> findAccidentById(int id);

    List<Accident> findAllAccidents();

    AccidentType findAccidentTypeById(int id);

    List<AccidentType> findAllAccidentTypes();

    void deleteAccidentById(int id);

    Rule findRuleById(int id);

    List<Rule> findAllRules();
}
