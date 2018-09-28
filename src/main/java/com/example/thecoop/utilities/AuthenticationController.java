package com.example.thecoop.utilities;

import com.example.thecoop.domain.User;
import com.example.thecoop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author iveshtard
 * @since 9/14/2018
 */

@Slf4j
public class AuthenticationController implements Runnable{
    private static int PERIOD = 5;

    public static Map<User, Boolean> authControl = new HashMap<>();
    private static boolean STARTED;

    @Autowired
    UserService userService;

    public static boolean isStarted() {
        return STARTED;
    }

    public static void setStarted(boolean started) {
        STARTED = started;
    }

    @Override
    public void run() {
        log.info("Authentication controller has been successfully started");

        while (true) {
            List<User> offLine = authControl
                    .keySet()
                    .stream()
                    .filter(visit -> new Date().after(DateUtils.addMinutes(visit.getLastVisit(), PERIOD)))
                    .collect(Collectors.toList());

            offLine.forEach(user -> authControl.put(user, false));
            log.info("authentication controller successfully checked users");

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
