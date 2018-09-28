package com.example.thecoop.service;

import com.example.thecoop.domain.Role;
import com.example.thecoop.domain.User;
import com.example.thecoop.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author iveshtard
 * @since 9/24/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private EmailSender mailSender;

    @Test
    public void addUser() {
        User user = new User();
        user.setEmail("some@mail.by");

        boolean isUserCreated = userService.addUser(user);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        assertFalse(user.isActive());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    public void addUserfailTest() {
        User user = new User();
        user.setUsername("Ivan");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Ivan"); // will return new user with name Ivan, to force method return false value

        boolean isUserCreated = userService.addUser(user);

        assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("bingo");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");
        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {

        boolean isUserActivated = userService.activateUser("activate me");
        assertFalse(isUserActivated);
        Mockito.verify(userRepo, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));

    }
}