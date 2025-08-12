package eu.itcrafter.playlist.persistence.song;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link Song}
 */
@Value
public class SongDto implements Serializable {
    @NotNull
    @Size(max = 50)
    String name;
    @NotNull
    @Size(max = 12)
    String mood;
    @NotNull
    LocalTime duration;
    @NotNull
    @Size(max = 20)
    String artist;
    @NotNull
    @Size(max = 12)
    String genre;
}