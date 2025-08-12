package eu.itcrafter.playlist.persistence.song;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "SONG")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Size(max = 12)
    @NotNull
    @Column(name = "MOOD", nullable = false, length = 12)
    private String mood;

    @NotNull
    @Column(name = "DURATION", nullable = false)
    private LocalTime duration;

    @Size(max = 20)
    @NotNull
    @Column(name = "ARTIST", nullable = false, length = 20)
    private String artist;

    @Size(max = 12)
    @NotNull
    @Column(name = "GENRE", nullable = false, length = 12)
    private String genre;

}