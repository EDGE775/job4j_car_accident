package ru.job4j.accident.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

//@Repository
public class AccidentJdbcTemplate implements AccidentRepository {

    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Accident saveOrUpdateAccident(Accident accident) {
        if (accident.getId() == 0) {
            final String INSERT_SQL = "insert into accidents (name, text, address, type_id, created) "
                    + "values (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                ps.setString(1, accident.getName());
                ps.setString(2, accident.getText());
                ps.setString(3, accident.getAddress());
                ps.setInt(4, accident.getType().getId());
                ps.setTimestamp(5, new Timestamp(accident.getCreated().getTime()));
                return ps;
            }, keyHolder);
            accident.setId(keyHolder.getKey().intValue());
            for (Rule rule : accident.getRules()) {
                jdbc.update("insert into accidents_rules (rule_id, accident_id) "
                                + "values (?, ?)",
                        rule.getId(),
                        accident.getId());
            }
        } else {
            final String INSERT_SQL = "update accidents set name = ?, text = ?, address = ?, type_id = ? where id = ?";
            jdbc.update(INSERT_SQL,
                    accident.getName(),
                    accident.getText(),
                    accident.getAddress(),
                    accident.getType().getId(),
                    accident.getId());

            jdbc.update(
                    "delete from accidents_rules where accident_id = ?",
                    accident.getId());

            for (Rule rule : accident.getRules()) {
                jdbc.update("insert into accidents_rules (rule_id, accident_id) "
                                + "values (?, ?)",
                        rule.getId(),
                        accident.getId());
            }
        }
        return accident;
    }

    @Override
    public Collection<Accident> findAllAccidents() {
        final String SELECT_SQL = "select a.id as a_id, a.name as a_name, a.text as a_text, "
                + "a.address as a_address, a.type_id as a_type_id, a.created as a_created, "
                + "t.id as t_id, t.name as t_name "
                + "from accidents as a left join types as t on a.type_id = t.id";
        List<Accident> accidents = new ArrayList<>();
        accidents = jdbc.query(SELECT_SQL,
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("a_id"));
                    accident.setName(rs.getString("a_name"));
                    accident.setText(rs.getString("a_text"));
                    accident.setAddress(rs.getString("a_address"));
                    accident.setCreated(rs.getTimestamp("a_created"));
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("t_id"));
                    accidentType.setName(rs.getString("t_name"));
                    accident.setType(accidentType);
                    return accident;
                });
        for (Accident accident : accidents) {
            List<Rule> rules = jdbc.query("select r.id as r_id, r.name as r_name from rules r "
                            + "left join accidents_rules ar on r.id = ar.rule_id where ar.accident_id = ?",
                    (rs, row) -> {
                        Rule rule = new Rule();
                        rule.setId(rs.getInt("r_id"));
                        rule.setName(rs.getString("r_name"));
                        return rule;
                    }, accident.getId());
            accident.setRules(new HashSet<>(rules));
        }
        return accidents;
    }

    @Override
    public Accident findAccidentById(int id) {
        final String SELECT_SQL = "select a.id as a_id, a.name as a_name, a.text as a_text, "
                + "a.address as a_address, a.type_id as a_type_id, a.created as a_created, "
                + "t.id as t_id, t.name as t_name "
                + "from accidents a left join types t on a.type_id = t.id where a.id = ?";
        Accident accident = jdbc.queryForObject(SELECT_SQL,
                (rs, row) -> {
                    Accident acc = new Accident();
                    acc.setId(rs.getInt("a_id"));
                    acc.setName(rs.getString("a_name"));
                    acc.setText(rs.getString("a_text"));
                    acc.setAddress(rs.getString("a_address"));
                    acc.setCreated(rs.getTimestamp("a_created"));
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("t_id"));
                    accidentType.setName(rs.getString("t_name"));
                    acc.setType(accidentType);
                    return acc;
                }, id);
        if (accident == null) {
            throw new IllegalArgumentException(String.format("Заявления с id: %s не существует!", id));
        }
        List<Rule> rules = jdbc.query("select r.id as r_id, r.name as r_name from rules r "
                        + "left join accidents_rules ar on r.id = ar.rule_id where ar.accident_id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("r_id"));
                    rule.setName(rs.getString("r_name"));
                    return rule;
                }, id);
        accident.setRules(new HashSet<>(rules));
        return accident;
    }

    @Override
    public void deleteAccidentById(int id) {
        jdbc.update(
                "delete from accidents_rules where accident_id = ?",
                id);
        jdbc.update(
                "delete from accidents where id = ?",
                id);
    }

    @Override
    public Collection<AccidentType> findAllAccidentTypes() {
        return jdbc.query("select id, name from types",
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return type;
                });
    }

    @Override
    public AccidentType findAccidentTypeById(int id) {
        AccidentType accidentType = jdbc.queryForObject("select id, name from types where id = ?",
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return type;
                }, id);
        if (accidentType == null) {
            throw new IllegalArgumentException(String.format("Типа с id: %s не существует!", id));
        }
        return accidentType;
    }

    @Override
    public Collection<Rule> findAllRules() {
        return jdbc.query("select id, name from rules",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    @Override
    public Rule findRuleById(int id) {
        Rule ruleRsl = jdbc.queryForObject("select id, name from rules where id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, id);
        if (ruleRsl == null) {
            throw new IllegalArgumentException(String.format("Правила с id: %s не существует!", id));
        }
        return ruleRsl;
    }
}
