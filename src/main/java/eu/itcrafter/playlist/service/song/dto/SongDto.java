package eu.itcrafter.playlist.service.song.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class SongDto {
    private Integer id;
    private String name;
    private String mood;
    private String duration;
    private String artist;
    private String genre;
}