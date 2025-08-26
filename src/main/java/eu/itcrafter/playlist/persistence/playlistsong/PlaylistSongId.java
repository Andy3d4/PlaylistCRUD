package eu.itcrafter.playlist.persistence.playlistsong;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlaylistSongId implements Serializable {
    private Integer playlistId;
    private Integer songId;
}