package eu.itcrafter.playlist.service.user;

import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.persistence.user.UserRepository;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.itcrafter.playlist.utils.Error.USER_ALREADY_EXISTS;
import static eu.itcrafter.playlist.utils.Error.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDtoList(users);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));
    }

    public void updateUser(Integer userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));
        user.setUsername(name);
        userRepository.save(user);
    }

    public void addUser(String name) {
        if (userRepository.existsByUsername(name)) {
            throw new DatabaseConstraintException(USER_ALREADY_EXISTS.getMessage());
        }
        User user = new User();
        user.setUsername(name);
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));
        userRepository.delete(user);
    }

}