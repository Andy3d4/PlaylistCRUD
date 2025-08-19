package eu.itcrafter.playlist.controller.user;

import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import eu.itcrafter.playlist.service.user.UserService;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all-users")
    @Operation(summary = "Returns a list of all users",
            description = "If there are no users, responds with an empty array.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a user based on its ID",
            description = "If there are no users, returns an empty array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public User getAllPlaylistSongs(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
