package eu.itcrafter.playlist.service.user;

import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.persistence.user.UserRepository;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("TestUser");

        testUserDto = new UserDto();
        testUserDto.setId(1);
        testUserDto.setUsername("TestUser");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers_WhenUsersExist() {
        // Given
        List<User> users = List.of(testUser);
        List<UserDto> expectedUserDtos = List.of(testUserDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserDtoList(users)).thenReturn(expectedUserDtos);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedUserDtos, result);

        verify(userRepository).findAll();
        verify(userMapper).toUserDtoList(users);
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        List<User> emptyUserList = new ArrayList<>();
        List<UserDto> emptyUserDtoList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(emptyUserList);
        when(userMapper.toUserDtoList(emptyUserList)).thenReturn(emptyUserDtoList);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository).findAll();
        verify(userMapper).toUserDtoList(emptyUserList);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());

        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Given
        Integer userId = 999;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    void updateUser_ShouldUpdateUsername_WhenUserExists() {
        // Given
        Integer userId = 1;
        String newUsername = "UpdatedUser";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(userId, newUsername);

        // Then
        assertEquals(newUsername, testUser.getUsername());

        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Given
        Integer userId = 999;
        String newUsername = "UpdatedUser";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, newUsername));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ShouldHandleNullUsername() {
        // Given
        Integer userId = 1;
        String nullUsername = null;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(userId, nullUsername);

        // Then
        assertNull(testUser.getUsername());

        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_ShouldHandleEmptyUsername() {
        // Given
        Integer userId = 1;
        String emptyUsername = "";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(userId, emptyUsername);

        // Then
        assertEquals(emptyUsername, testUser.getUsername());

        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void addUser_ShouldCreateUser_WhenUsernameDoesNotExist() {
        // Given
        String username = "NewUser";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.addUser(username);

        // Then
        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_ShouldThrowDatabaseConstraintException_WhenUsernameAlreadyExists() {
        // Given
        String existingUsername = "ExistingUser";

        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        // When & Then
        DatabaseConstraintException exception = assertThrows(DatabaseConstraintException.class, () -> userService.addUser(existingUsername));

        assertNotNull(exception.getMessage());

        verify(userRepository).existsByUsername(existingUsername);
        verify(userRepository, never()).save(any());
    }

    @Test
    void addUser_ShouldHandleNullUsername() {
        // Given
        String nullUsername = null;

        when(userRepository.existsByUsername(nullUsername)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.addUser(nullUsername);

        // Then
        verify(userRepository).existsByUsername(nullUsername);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_ShouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";

        when(userRepository.existsByUsername(emptyUsername)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.addUser(emptyUsername);

        // Then
        verify(userRepository).existsByUsername(emptyUsername);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_ShouldHandleWhitespaceUsername() {
        // Given
        String whitespaceUsername = "   ";

        when(userRepository.existsByUsername(whitespaceUsername)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.addUser(whitespaceUsername);

        // Then
        verify(userRepository).existsByUsername(whitespaceUsername);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Given
        Integer userId = 999;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_ShouldHandleNullUserId() {
        // Given
        Integer nullUserId = null;

        when(userRepository.findById(nullUserId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(nullUserId));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(nullUserId);
        verify(userRepository, never()).delete(any());
    }

    // Additional edge case tests

    @Test
    void getAllUsers_ShouldHandleLargeUserList() {
        // Given
        List<User> largeUserList = new ArrayList<>();
        List<UserDto> largeDtoList = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("User" + i);
            largeUserList.add(user);

            UserDto dto = new UserDto();
            dto.setId(i);
            dto.setUsername("User" + i);
            largeDtoList.add(dto);
        }

        when(userRepository.findAll()).thenReturn(largeUserList);
        when(userMapper.toUserDtoList(largeUserList)).thenReturn(largeDtoList);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1000, result.size());

        verify(userRepository).findAll();
        verify(userMapper).toUserDtoList(largeUserList);
    }

    @Test
    void updateUser_ShouldHandleSpecialCharactersInUsername() {
        // Given
        Integer userId = 1;
        String specialUsername = "User@#$%^&*()_+";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(userId, specialUsername);

        // Then
        assertEquals(specialUsername, testUser.getUsername());

        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void addUser_ShouldHandleVeryLongUsername() {
        // Given
        String longUsername = "a".repeat(255);

        when(userRepository.existsByUsername(longUsername)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.addUser(longUsername);

        // Then
        verify(userRepository).existsByUsername(longUsername);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_ShouldHandleZeroUserId() {
        // Given
        Integer zeroUserId = 0;

        when(userRepository.findById(zeroUserId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(zeroUserId));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(zeroUserId);
    }

    @Test
    void getUserById_ShouldHandleNegativeUserId() {
        // Given
        Integer negativeUserId = -1;

        when(userRepository.findById(negativeUserId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(negativeUserId));

        assertNotNull(exception.getMessage());

        verify(userRepository).findById(negativeUserId);
    }
}