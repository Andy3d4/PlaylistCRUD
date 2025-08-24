package eu.itcrafter.playlist.persistence.playlist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PlaylistSong (playlistId, songId) VALUES (:playlistId, :songId)", nativeQuery = true)
    void addSongToPlaylist(Integer playlistId, Integer songId);


}