package eu.itcrafter.playlist.persistence.playlist;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.persistence.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PLAYLIST")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @NotNull
    @Column(name = "USERID", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "CREATEDAT", nullable = false)
    private LocalDate createdat;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PlaylistSong",
            joinColumns = @JoinColumn(name = "playlistId"),
            inverseJoinColumns = @JoinColumn(name = "songId")
    )
    private List<Song> songs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}