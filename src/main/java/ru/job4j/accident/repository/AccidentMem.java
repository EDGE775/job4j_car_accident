package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {

    private static final AtomicInteger ACCIDENT_ID = new AtomicInteger(0);

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem() {
        this.saveOrUpdateAccident(
                new Accident("Заявка 1", "Неправильная парковка", "Ул. Кукунина"));
        this.saveOrUpdateAccident(
                new Accident("Заявка 2", "Ударил авто", "Ул. Волчухина"));
        this.saveOrUpdateAccident(
                new Accident("Заявка 3", "Заехал на газон", "Ул. Мичурина"));
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
}
