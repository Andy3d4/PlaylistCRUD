package eu.itcrafter.playlist.service.playlist.dto;

import eu.itcrafter.playlist.service.song.dto.SongDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PlaylistWithSongsDto {
    private Integer id;
    private String name;
    @NotNull
    private LocalDate createdat;
    private List<SongDto> songs;
}