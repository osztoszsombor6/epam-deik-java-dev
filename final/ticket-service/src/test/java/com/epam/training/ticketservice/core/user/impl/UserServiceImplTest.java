/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author zsombor2
 */
public class UserServiceImplTest {
    
    private final UserRepository userRepository = mock(UserRepository.class);
    private UserService underTest = new UserServiceImpl(userRepository);

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.login(null, "pass"));
    }

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.login("user", null));
    }

    @Test
    public void testLoginShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        // Given
        User user = new User("user", "password", User.Role.USER);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> actual = underTest.login("user", "pass");

        // Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPassword("user", "pass");
    }

    @Test
    public void testLoginShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPassword("dummy", "dummy")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTest.login("dummy", "dummy");

        // Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPassword("dummy", "dummy");
    }

    @Test
    public void testLogoutShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testLogoutShouldReturnThePreviouslyLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        UserDto user = new UserDto("user", User.Role.USER);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLoggedInUserShouldReturnTheLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        UserDto user = new UserDto("user", User.Role.USER);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLoggedInUserShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testRegisterUserShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.registerUser(null, "pass"));
    }

    @Test
    public void testRegisterUserShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.registerUser("user", null));
    }

    @Test
    public void testRegisterUserShouldCallUserRepositoryWhenTheInputIsValid() {
        // Given
        // When
        underTest.registerUser("user", "pass");

        // Then
        verify(userRepository).save(new User("user", "pass", User.Role.USER));
    }
    
    @Test
    public void testGetUserRole() {
        // Given
        User user = new User("user", "password", User.Role.USER);
        User.Role expected = User.Role.USER;
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        // When
        User.Role actual = underTest.getUserRole("user");
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetUserRoleWithInvalidUser() {
        // Given
        User.Role expected = null;
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        // When
        User.Role actual = underTest.getUserRole("user");
        
        //Then
        assertEquals(expected, actual);
    }
    
}
