package com.example.thecoop.utilities;

import com.example.thecoop.domain.User;
import com.example.thecoop.repos.UserRepo;
import com.example.thecoop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author iveshtard
 * @since 9/14/2018
 */

@Slf4j
@Component
@EnableScheduling
public class AuthenticationController{
    public static Set<User> onlineUsers;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SessionRegistry sessionRegistry;

    @Scheduled(initialDelay = 10, fixedDelay = 1000)
    public void fillOnlineUserList(){
        onlineUsers = new HashSet<>();

        sessionRegistry.getAllPrincipals().stream().filter((Object principal) -> !this.sessionRegistry
                .getAllSessions(principal, false).isEmpty()).forEach(onlnUsr -> onlineUsers
                .add((User)onlnUsr));

    }

    @Scheduled(initialDelay = 1000, fixedDelay = 5 * 60 * 1000)
    public void authControl() {
        onlineUsers.forEach(user -> user.setLastVisit(new Date()));
        onlineUsers.forEach(user -> userRepo.save(user));
        log.info("authentication controller successfully checked users");
    }
}
