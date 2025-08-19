package eu.itcrafter.playlist.service.user;

import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> users);
}
