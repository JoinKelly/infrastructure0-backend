package com.infrastructure.backend.controller;

import com.infrastructure.backend.entity.User;
import com.infrastructure.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    @ResponseBody
    public String addNewUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(newUser);
        return "Saved";
    }
}
