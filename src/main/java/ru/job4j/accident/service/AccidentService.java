package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccidentService {

    private final AccidentRepository accidentRepository;

    public AccidentService(AccidentRepository accidentRepository) {
        this.accidentRepository = accidentRepository;
    }

    public Accident saveOrUpdateAccident(Accident accident) {
        if (accident == null) {
            throw new IllegalArgumentException("Аргумент не можеть быть null");
        }
        return accidentRepository.saveOrUpdateAccident(accident);
    }

    public Accident findAccidentById(int id) {
        return accidentRepository.findAccidentById(id);
    }

    public List<Accident> findAllAccidents() {
        return accidentRepository.findAllAccidents()
                .stream()
                .sorted(Comparator.comparingInt(Accident::getId))
                .collect(Collectors.toList());
    }

    public AccidentType findAccidentTypeById(int id) {
        return accidentRepository.findAccidentTypeById(id);
    }

    public List<AccidentType> findAllAccidentTypes() {
        return accidentRepository.findAllAccidentTypes()
                .stream()
                .sorted(Comparator.comparingInt(AccidentType::getId))
                .collect(Collectors.toList());
    }

    public void deleteAccidentById(int id) {
        accidentRepository.deleteAccidentById(id);
    }

    public Rule findRuleById(int id) {
        return accidentRepository.findRuleById(id);
    }

    public List<Rule> findAllRules() {
        return accidentRepository.findAllRules()
                .stream()
                .sorted(Comparator.comparingInt(Rule::getId))
                .collect(Collectors.toList());
    }
}
