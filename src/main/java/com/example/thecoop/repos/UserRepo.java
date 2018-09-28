package com.example.thecoop.repos;

import com.example.thecoop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
}
