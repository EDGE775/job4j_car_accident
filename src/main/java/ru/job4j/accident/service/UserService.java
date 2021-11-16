package ru.job4j.accident.service;

import ru.job4j.accident.model.Authority;
import ru.job4j.accident.model.User;

public interface UserService {

    Authority findByAuthorityName(String name);

    User saveUser(User user);
}
