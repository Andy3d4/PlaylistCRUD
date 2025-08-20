package eu.itcrafter.playlist.controller.song;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import eu.itcrafter.playlist.service.song.SongService;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/song")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("all-songs")
    @Operation(summary = "Returns a list of all songs",
            description = "If there are no songs, responds with an empty array.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a song based on its ID",
            description = "If there are no songs, returns an empty array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})
    public SongDto getAllPlaylistSongs(@PathVariable int id) {
        return songService.getSongById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates the songs information.",
            description = """
                    Updates the song information the system.
                    If the playlist does not exist, responds with error code 409 (CONFLICT).""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Song name already exists")})
    public void updateSong(@PathVariable("id") Integer id, @RequestBody SongDto songDto) {
        songService.updateSong(id, songDto);
    }
}
