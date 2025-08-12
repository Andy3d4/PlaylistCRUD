package eu.itcrafter.playlist.persistence.playlist;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    @Column(name = "CREATEDAT", nullable = false)
    private LocalDate createdat;

}