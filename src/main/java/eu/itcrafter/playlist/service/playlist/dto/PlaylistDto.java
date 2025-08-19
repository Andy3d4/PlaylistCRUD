package eu.itcrafter.playlist.service.playlist.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link eu.itcrafter.playlist.persistence.playlist.Playlist}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto implements Serializable {
    private Integer id;
    @NotNull
    @Size(max = 20)
    private String name;
    @NotNull
    private LocalDate createdat;
    private String username;

}