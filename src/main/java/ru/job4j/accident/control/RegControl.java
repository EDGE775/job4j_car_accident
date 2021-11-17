package ru.job4j.accident.control;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.User;
import ru.job4j.accident.repository.AuthorityRepository;
import ru.job4j.accident.repository.UserRepository;
import ru.job4j.accident.service.UserService;

@Controller
public class RegControl {

    private final PasswordEncoder encoder;
    private final UserService userService;

    public RegControl(PasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @PostMapping("/reg")
    public String regSave(@ModelAttribute User user) {
        if (userService.findUserByName(user.getUsername()) != null) {
            return "redirect:/reg?error=true";
        }
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(userService.findByAuthorityName("ROLE_USER"));
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String regPage(@RequestParam(value = "error", required = false) String error,
                          Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "The same name is used !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "reg";
    }
}