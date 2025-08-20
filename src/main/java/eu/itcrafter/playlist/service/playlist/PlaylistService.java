package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.persistence.playlist.PlaylistRepository;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.itcrafter.playlist.utils.Error.PLAYLIST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistMapper playlistMapper;
    private final PlaylistRepository playlistRepository;

    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlist = playlistRepository.findAll();
        return playlistMapper.toPlaylistDtoList(playlist);
    }

    public PlaylistWithSongsDto getAllSongsInPlaylist(int id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND.getMessage()));
        return playlistMapper.toPlaylistWithSongsDto(playlist);
    }
    public void updatePlaylistName(int id, String name){
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND.getMessage()));
        playlist.setName(name);
        playlistRepository.save(playlist);
    }

}
