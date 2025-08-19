package eu.itcrafter.playlist.controller.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.service.playlist.PlaylistService;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
