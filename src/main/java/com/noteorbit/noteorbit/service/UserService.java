package com.noteorbit.noteorbit.service;

import com.noteorbit.noteorbit.entity.User;
import com.noteorbit.noteorbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🔐 Register User
    public String registerUser(User user) {

        // ✅ Check if email already exists
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "Email already registered! Try login.";
        }

        String password = user.getPassword();

        // ✅ Password minimum length check
        if (password.length() < 6) {
            return "Password must be at least 6 characters long!";
        }

        // ✅ Strong password check
        if (!isStrongPassword(password)) {
            return "Password must contain uppercase, lowercase, number, and special character!";
        }

        // ✅ Save user
        userRepository.save(user);
        return "Registration Successful!";
    }

    // 🔐 Login User
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user; // success
        }

        return null; // fail
    }
    
    public User updateUser(User updatedUser){

        User user = userRepository.findByEmail(updatedUser.getEmail());

        if(user == null) return null;

        user.setName(updatedUser.getName());
        user.setCollege(updatedUser.getCollege());
        user.setDept(updatedUser.getDept());
        user.setYear(updatedUser.getYear());
        user.setClassName(updatedUser.getClassName());

        // 🔥 PHOTO FIX (IMPORTANT)
        if(updatedUser.getPhoto() != null && !updatedUser.getPhoto().isEmpty()){
            user.setPhoto(updatedUser.getPhoto());
        }

        return userRepository.save(user);
    }

    // 🔒 Strong Password Validation Method
    private boolean isStrongPassword(String password) {

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}