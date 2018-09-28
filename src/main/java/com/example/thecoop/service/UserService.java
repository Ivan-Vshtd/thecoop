package com.example.thecoop.service;

import com.example.thecoop.domain.Role;
import com.example.thecoop.domain.User;
import com.example.thecoop.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("localhost:8080") //${hostname} changed for tests, needs to return
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        log.info(username + " has been found in db");
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        } else {
            user.setRoles(Collections.singleton(Role.USER));
            user.setActivationCode(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepo.save(user);
            log.info(user.getUsername() + " has been successfully added to db");

            sendMessage(user);
        }
        return true;
    }

    private void sendMessage(User user) {
        if (!Strings.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello! %s\nWelcome to the Coop.\nPlease visit http://%s/activate/%s to activate your profile!"
                    , user.getUsername(), hostname, user.getActivationCode());

            emailSender.send(user.getEmail(), "ActivationCode", message);
            log.debug("activation code sent to " + user.getUsername() + " via email");
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);
        log.debug(user.getUsername() + " has been successfully activated");

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles =                    // get all available user's roles as String
                Arrays
                        .stream(Role.values())
                        .map(Role::name)
                        .collect(Collectors.toSet());

        user.getRoles().clear();                // clear old roles before adding new one

        form                                    // check if enum have such role and add it to user before saving
                .keySet()
                .stream()
                .filter(roles::contains)
                .forEach(key -> user
                        .getRoles()
                        .add(Role.valueOf(key)));

        userRepo.save(user);
        log.debug(user.getUsername() + " has been successfully saved to db");
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);
            if (!Strings.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!Strings.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
        log.debug(user.getUsername() + " has been successfully updated in db");
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepo.save(user);
        log.info(currentUser.getUsername() + " has been added to subscribers of " + user.getUsername());
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
        log.info(currentUser.getUsername() + " has been removed from subscribers of " + user.getUsername());
    }

    public User loadUserById(Long id){
        return userRepo.findById(id).get();
    }
}
