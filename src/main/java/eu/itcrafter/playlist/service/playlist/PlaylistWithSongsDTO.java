package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.service.song.SongDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PlaylistWithSongsDTO {
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private List<SongDTO> songs;
}