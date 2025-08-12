package eu.itcrafter.playlist.service.song;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class SongDTO {
    private Integer id;
    private String name;
    private String mood;
    private LocalTime duration;
    private String artist;
    private String genre;
}