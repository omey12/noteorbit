package com.noteorbit.noteorbit.repository;

import com.noteorbit.noteorbit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
