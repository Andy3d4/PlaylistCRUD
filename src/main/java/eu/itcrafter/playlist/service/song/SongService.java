package eu.itcrafter.playlist.service.song;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.persistence.song.SongDto;
import eu.itcrafter.playlist.persistence.song.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;

    public List<Song>getAllSongs(){
        return songRepository.findAll();
    }
}
