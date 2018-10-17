package com.example.thecoop.utilities;

import com.example.thecoop.domain.User;
import com.example.thecoop.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

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
    private Map<User, Date> visitors;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SessionRegistry sessionRegistry;

    @Scheduled(initialDelay = 100, fixedDelay = 1000)
    public void fillOnlineUserList(){
        onlineUsers = new HashSet<>();

        onlineUsers.addAll(
                    visitors
                        .keySet()
                        .stream()
                        .filter(visit -> !new Date().after(DateUtils.addMinutes(visit.getLastVisit(), 5)))
                        .collect(Collectors.toList()));
    }

    @Scheduled(fixedDelay = 1000)
    public void authControl() {
        visitors = new HashMap<>();

        sessionRegistry
                .getAllPrincipals()
                .forEach(principal -> visitors.put((User)principal,
                        sessionRegistry
                                .getAllSessions(principal, true)
                                .stream()
                                .map(SessionInformation::getLastRequest)
                                .max(Date::compareTo)
                                .get()));

        visitors.keySet().forEach(user -> user.setLastVisit(visitors.get(user)));
        visitors.keySet().forEach(user -> userRepo.save(user));
    }
}
