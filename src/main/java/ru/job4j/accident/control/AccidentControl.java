package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AccidentControl {
    private final AccidentService accidentService;

    public AccidentControl(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<AccidentType> types = accidentService.findAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = accidentService.findAllRules();
        model.addAttribute("rules", rules);
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.findAccidentById(id).get());
        List<AccidentType> types = accidentService.findAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = accidentService.findAllRules();
        model.addAttribute("rules", rules);
        return "accident/update";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        String[] ids = req.getParameterValues("rIds");
        accidentService.saveOrUpdateAccident(accident, ids);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        accidentService.deleteAccidentById(id);
        return "redirect:/";
    }
}