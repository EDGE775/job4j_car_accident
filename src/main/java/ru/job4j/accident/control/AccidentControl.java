package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AccidentControl {
    private final AccidentService service;

    public AccidentControl(AccidentService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<AccidentType> types = service.findAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = service.findAllRules();
        model.addAttribute("rules", rules);
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", service.findAccidentById(id));
        List<AccidentType> types = service.findAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = service.findAllRules();
        model.addAttribute("rules", rules);
        return "accident/update";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        accident.setType(service.findAccidentTypeById(accident.getType().getId()));
        String[] ids = req.getParameterValues("rIds");
        if (ids != null) {
            Set<Rule> rules = new HashSet<>();
            for (String id : ids) {
                Rule rule = service.findRuleById(Integer.parseInt(id));
                rules.add(rule);
            }
            accident.setRules(rules);
        }
        service.saveOrUpdateAccident(accident);
        return "redirect:/";
    }
}