package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.persistence.playlist.PlaylistRepository;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistMapper playlistMapper;
    private final PlaylistRepository playlistRepository;

    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlist = playlistRepository.findAll();
        return playlistMapper.toPlaylistDtoList(playlist);
    }
}
