package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.persistence.playlist.PlaylistRepository;
import eu.itcrafter.playlist.persistence.playlistsong.PlaylistSong;
import eu.itcrafter.playlist.persistence.playlistsong.PlaylistSongRepository;
import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.persistence.user.UserRepository;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.itcrafter.playlist.utils.Error.*;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final PlaylistMapper playlistMapper;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;


    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlist = playlistRepository.findAll();
        return playlistMapper.toPlaylistDtoList(playlist);
    }

    public PlaylistWithSongsDto getAllSongsInPlaylist(int id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND.getMessage()));
        return playlistMapper.toPlaylistWithSongsDto(playlist);
    }

    public void updatePlaylistName(int id, String name) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND.getMessage()));
        playlist.setName(name);
        playlistRepository.save(playlist);
    }

    public void addPlaylist(PlaylistDto playlistDto) {
        if (playlistDto.getUsername() == null || playlistDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        User user = userRepository.findByUsername(playlistDto.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setCreatedat(playlistDto.getCreatedat());
        playlist.setUser(user);
        playlistRepository.save(playlist);
    }


    public void addSongToPlaylist(Integer playlistId, Integer songId) {
        if (!playlistRepository.existsById(playlistId)) {
            throw new DatabaseConstraintException(PLAYLIST_NOT_FOUND.getMessage());
        }

        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            throw new DatabaseConstraintException(SONG_ALREADY_EXISTS.getMessage());
        }

        PlaylistSong playlistSong = new PlaylistSong(playlistId, songId);
        playlistSongRepository.save(playlistSong);
    }
    public void deletePlaylist(int id) {
        if (!playlistRepository.existsById(id)) {
            throw new ResourceNotFoundException(PLAYLIST_NOT_FOUND.getMessage());
        }
        playlistRepository.deleteById(id);
    }

    public void deleteSongFromPlaylist(int playlistId, int songId) {
        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song or Playlist not found"));

        playlistSongRepository.delete(playlistSong);
    }
}
