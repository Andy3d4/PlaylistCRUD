package eu.itcrafter.playlist.controller.user;

import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import eu.itcrafter.playlist.service.user.UserService;
import eu.itcrafter.playlist.service.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    @Operation(summary = "Updates the users name",
            description = """
                    Updates the users name in the system.
                    If the user does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Song name already exists")})
    public void updateUser(@PathVariable("id") Integer id, @RequestParam String name) {
        userService.updateUser(id, name);
    }
}
