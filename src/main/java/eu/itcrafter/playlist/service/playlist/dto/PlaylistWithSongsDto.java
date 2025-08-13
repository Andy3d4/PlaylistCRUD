package eu.itcrafter.playlist.service.playlist.dto;

import eu.itcrafter.playlist.service.song.dto.SongDto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PlaylistWithSongsDto {
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private List<SongDto> songs;
}