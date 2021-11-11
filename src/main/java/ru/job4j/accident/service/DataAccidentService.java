package ru.job4j.accident.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;
import ru.job4j.accident.repository.AccidentTypeRepository;
import ru.job4j.accident.repository.RuleRepository;

import java.util.*;

@Primary
@Service
public class DataAccidentService implements ServiceInterface {

    private final AccidentRepository accidentRepository;

    private final AccidentTypeRepository accidentTypeRepository;

    private final RuleRepository ruleRepository;

    public DataAccidentService(AccidentRepository accidentRepository,
                               AccidentTypeRepository accidentTypeRepository,
                               RuleRepository ruleRepository) {
        this.accidentRepository = accidentRepository;
        this.accidentTypeRepository = accidentTypeRepository;
        this.ruleRepository = ruleRepository;
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
        return accidentRepository.save(accident);
    }

    @Override
    public Optional<Accident> findAccidentById(int id) {
        return accidentRepository.findById(id);
    }

    @Override
    public List<Accident> findAllAccidents() {
        return accidentRepository.findAllByOrderByIdAsc();
    }

    @Override
    public AccidentType findAccidentTypeById(int id) {
        return accidentTypeRepository.findById(id);
    }

    @Override
    public List<AccidentType> findAllAccidentTypes() {
        return accidentTypeRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void deleteAccidentById(int id) {
        if (!accidentRepository.existsById(id)) {
            throw new IllegalArgumentException("Запись с таким id не найдена!");
        }
        accidentRepository.deleteById(id);
    }

    @Override
    public Rule findRuleById(int id) {
        return ruleRepository.findById(id);
    }

    @Override
    public List<Rule> findAllRules() {
        return ruleRepository.findAllByOrderByIdAsc();
    }
}
