package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {

    private static final AtomicInteger ACCIDENT_ID = new AtomicInteger(0);

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private final Map<Integer, AccidentType> types = new ConcurrentHashMap<>();

    public AccidentMem() {
        this.saveAccidentType(AccidentType.of(1, "Две машины"));
        this.saveAccidentType(AccidentType.of(2, "Машина и человек"));
        this.saveAccidentType(AccidentType.of(3, "Машина и велосипед"));

        this.saveOrUpdateAccident(
                new Accident("Заявка 1", "Неправильная парковка", "Ул. Кукунина", types.get(1)));
        this.saveOrUpdateAccident(
                new Accident("Заявка 2", "Ударил авто", "Ул. Волчухина", types.get(2)));
        this.saveOrUpdateAccident(
                new Accident("Заявка 3", "Заехал на газон", "Ул. Мичурина", types.get(3)));

    }

    public Accident saveOrUpdateAccident(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(ACCIDENT_ID.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
        return accident;
    }

    public Collection<Accident> findAllAccidents() {
        return accidents.values();
    }

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return types.values();
    }

    public void saveAccidentType(AccidentType type) {
        types.put(type.getId(), type);
    }

    public AccidentType findAccidentTypeById(int id) {
        return types.get(id);
    }
}
