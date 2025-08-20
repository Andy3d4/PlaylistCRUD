package eu.itcrafter.playlist.controller.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.service.playlist.PlaylistService;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/playlist")
public class PlaylistController {

private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/all")
    @Operation(summary = "Returns a list of all playlists",
            description = "If there are no playlist, returns an empty array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public List<PlaylistDto> getAllPlaylist() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/playlist-songs/{id}")
    @Operation(summary = "Returns a list of all the songs inside playlist",
            description = "If there are no songs, returns an empty array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public PlaylistWithSongsDto getAllPlaylistSongs(@PathVariable int id) {
        return playlistService.getAllSongsInPlaylist(id);
    }
    /*@PostMapping
    @Operation(summary = "Adds a new genre.",
            description = """
                    Creates a new genre in the system.
                    If the genre already exists, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Genre already exists")})
    public void addGenre(@RequestParam String genreName) {
        genreService.addGenre(genreName);
    }*/

    @PutMapping("/{id}")
    @Operation(summary = "Updates the playlist name.",
            description = """
                    Updates the playlist name in the system.
                    If the playlist does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Playlist does not exist")})
    public void updatePlaylistName(@PathVariable("id") Integer id, @RequestParam String name) {
        playlistService.updatePlaylistName(id, name);
    }
    /*@DeleteMapping("/{id}")
    @Operation(summary = "Deletes the genre by id. If the genre is associated with a movie, deletion is not allowed.",
            description = """
                    Deletes the genre name from the system.
                    If the genre does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public void deleteGenre(@PathVariable("id") Integer id) {
        genreService.deleteGenreBy(id);
    }*/
}


