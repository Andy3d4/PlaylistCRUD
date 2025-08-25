package eu.itcrafter.playlist.controller.playlist;

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

    @PostMapping("/add-playlist")
    @Operation(summary = "Adds a new playlist.",
            description = """
                    Creates a new playlist in the system for user.
                    If the playlist already exists, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Playlist already exists")})
    public void addPlaylist(@RequestBody PlaylistDto playlistDto) {
        playlistService.addPlaylist(playlistDto);
    }

    @PostMapping("/add-song-to-playlist/{playlistId}/{songId}")
    @Operation(summary = "Adds a new song to playlist.",
            description = """
                    Adds a song in the playlist.
                    If the song already exists, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Song already exists")})
    public void addSongToPlaylist(@PathVariable("playlistId") Integer playlistId, @PathVariable("songId") Integer songId) {
        playlistService.addSongToPlaylist(playlistId, songId);
    }

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

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes the playlist by id. If the playlist is associated with a song, deletion is not allowed.",
            description = """
                    Deletes the playlist name from the system.
                    If the genre does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public void deletePlaylist(@PathVariable("id") Integer id) {
        playlistService.deletePlaylist(id);
    }

    @DeleteMapping("/{playlistId}/{songId}")
    @Operation(summary = "Deletes the song from the playlist by id.",
            description = """
                    Deletes the song from playlist.
                    If the genre does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public void deleteSongFromPlaylist(@PathVariable("playlistId") Integer playlistId, @PathVariable("songId") Integer songId) {
        playlistService.deleteSongFromPlaylist(songId, playlistId);
    }
}


