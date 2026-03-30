package com.noteorbit.noteorbit.controller;

import com.noteorbit.noteorbit.entity.User;
import com.noteorbit.noteorbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ Register API
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    
    @PutMapping("/update")
    public User update(@RequestBody User user){
        return userService.updateUser(user);
    }

    // ✅ Login API
    @PostMapping("/login")
    public User login(@RequestParam String email,
                      @RequestParam String password) {

        User user = userService.loginUser(email, password);

        if (user == null) {
            throw new RuntimeException("Invalid email or password!");
        }

        return user;
    }
}