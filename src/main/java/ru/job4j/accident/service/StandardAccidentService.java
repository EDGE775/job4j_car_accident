package ru.job4j.accident.service;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.StandardAccidentRepositoryInterface;

import java.util.*;
import java.util.stream.Collectors;

public class StandardAccidentService implements AccidentService {

    private final StandardAccidentRepositoryInterface accidentRepository;

    public StandardAccidentService(StandardAccidentRepositoryInterface accidentRepository) {
        this.accidentRepository = accidentRepository;
    }

    @Override
    public Accident saveOrUpdateAccident(Accident accident, String[] ids) {
        if (accident == null) {
            throw new IllegalArgumentException("Аргумент не можеть быть null");
        }
        Set<Rule> rules = new HashSet<>();
        for (String id : ids) {
            Rule rule = findRuleById(Integer.parseInt(id));
            rules.add(rule);
        }
        accident.setRules(rules);
        accident.setType(findAccidentTypeById(accident.getType().getId()));
        return accidentRepository.saveOrUpdateAccident(accident);
    }

    @Override
    public Optional<Accident> findAccidentById(int id) {
        Accident accident = accidentRepository.findAccidentById(id);
        return Optional.ofNullable(accident);
    }

    @Override
    public List<Accident> findAllAccidents() {
        return accidentRepository.findAllAccidents()
                .stream()
                .sorted(Comparator.comparingInt(Accident::getId))
                .collect(Collectors.toList());
    }

    @Override
    public AccidentType findAccidentTypeById(int id) {
        return accidentRepository.findAccidentTypeById(id);
    }

    @Override
    public List<AccidentType> findAllAccidentTypes() {
        return accidentRepository.findAllAccidentTypes()
                .stream()
                .sorted(Comparator.comparingInt(AccidentType::getId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccidentById(int id) {
        accidentRepository.deleteAccidentById(id);
    }

    @Override
    public Rule findRuleById(int id) {
        return accidentRepository.findRuleById(id);
    }

    @Override
    public List<Rule> findAllRules() {
        return accidentRepository.findAllRules()
                .stream()
                .sorted(Comparator.comparingInt(Rule::getId))
                .collect(Collectors.toList());
    }
}
