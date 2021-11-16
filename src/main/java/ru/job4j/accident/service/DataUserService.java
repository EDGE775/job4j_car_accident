package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Authority;
import ru.job4j.accident.model.User;
import ru.job4j.accident.repository.AuthorityRepository;
import ru.job4j.accident.repository.UserRepository;

@Service
public class DataUserService implements UserService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public DataUserService(UserRepository userRepository,
                           AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findByAuthorityName(String name) {
        return authorityRepository.findByAuthority(name);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
