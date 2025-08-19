package eu.itcrafter.playlist.persistence.playlist;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

}