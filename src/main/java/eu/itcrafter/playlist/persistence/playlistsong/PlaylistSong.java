package eu.itcrafter.playlist.persistence.playlistsong;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PlaylistSong")
@IdClass(PlaylistSongId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSong {
    @Id
    @Column(name = "playlistId")
    private Integer playlistId;

    @Id
    @Column(name = "songId")
    private Integer songId;
}