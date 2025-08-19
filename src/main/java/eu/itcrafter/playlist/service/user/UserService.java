package eu.itcrafter.playlist.service.user;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.persistence.user.UserRepository;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        return user;
    }

}