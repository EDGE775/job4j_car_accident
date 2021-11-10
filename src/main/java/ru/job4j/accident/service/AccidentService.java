package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentMem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccidentService {

    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    public Accident saveOrUpdateAccident(Accident accident) {
        if (accident == null) {
            throw new IllegalArgumentException("Аргумент не можеть быть null");
        }
        return accidentMem.saveOrUpdateAccident(accident);
    }

    public Accident findAccidentById(int id) {
        return accidentMem.findAccidentById(id);
    }

    public List<Accident> findAllAccidents() {
        return accidentMem.findAllAccidents()
                .stream()
                .sorted(Comparator.comparingInt(Accident::getId))
                .collect(Collectors.toList());
    }

    public AccidentType findAccidentTypeById(int id) {
        return accidentMem.findAccidentTypeById(id);
    }

        public List<AccidentType> findAllAccidentTypes() {
        return accidentMem.findAllAccidentTypes()
                .stream()
                .sorted(Comparator.comparingInt(AccidentType::getId))
                .collect(Collectors.toList());
    }

    public Rule findRuleById(int id) {
        return accidentMem.findRuleById(id);
    }

    public List<Rule> findAllRules() {
        return accidentMem.findAllRules()
                .stream()
                .sorted(Comparator.comparingInt(Rule::getId))
                .collect(Collectors.toList());
    }
}
