package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem implements AccidentRepository {

    private static final AtomicInteger ACCIDENT_ID = new AtomicInteger(0);

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private final Map<Integer, AccidentType> types = new ConcurrentHashMap<>();

    private final Map<Integer, Rule> rules = new ConcurrentHashMap<>();

    public AccidentMem() {
        this.saveAccidentType(AccidentType.of(1, "Две машины"));
        this.saveAccidentType(AccidentType.of(2, "Машина и человек"));
        this.saveAccidentType(AccidentType.of(3, "Машина и велосипед"));

        this.saveRule(Rule.of(1, "Статья. 1"));
        this.saveRule(Rule.of(2, "Статья. 2"));
        this.saveRule(Rule.of(3, "Статья. 3"));

        this.saveOrUpdateAccident(
                new Accident("Заявка 1", "Неправильная парковка", "Ул. Кукунина", types.get(1)));
        this.saveOrUpdateAccident(
                new Accident("Заявка 2", "Ударил авто", "Ул. Волчухина", types.get(2)));
        this.saveOrUpdateAccident(
                new Accident("Заявка 3", "Заехал на газон", "Ул. Мичурина", types.get(3)));

    }

    @Override
    public Accident saveOrUpdateAccident(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(ACCIDENT_ID.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
        return accident;
    }

    @Override
    public Collection<Accident> findAllAccidents() {
        return accidents.values();
    }

    @Override
    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    @Override
    public void deleteAccidentById(int id) {
        accidents.remove(id);
    }

    @Override
    public Collection<AccidentType> findAllAccidentTypes() {
        return types.values();
    }

    public void saveAccidentType(AccidentType type) {
        types.put(type.getId(), type);
    }

    @Override
    public AccidentType findAccidentTypeById(int id) {
        return types.get(id);
    }

    @Override
    public Collection<Rule> findAllRules() {
        return rules.values();
    }

    public void saveRule(Rule rule) {
        rules.put(rule.getId(), rule);
    }

    @Override
    public Rule findRuleById(int id) {
        return rules.get(id);
    }
}
