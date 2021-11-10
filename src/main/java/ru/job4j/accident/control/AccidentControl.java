package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.service.AccidentService;

import java.util.List;

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
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", service.findAccidentById(id));
        List<AccidentType> types = service.findAllAccidentTypes();
        model.addAttribute("types", types);
        return "accident/update";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        accident.setType(service.findAccidentTypeById(accident.getType().getId()));
        service.saveOrUpdateAccident(accident);
        return "redirect:/";
    }
}