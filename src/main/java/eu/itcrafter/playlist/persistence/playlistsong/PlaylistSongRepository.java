package eu.itcrafter.playlist.persistence.playlistsong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {

    boolean existsByPlaylistIdAndSongId(Integer playlistId, Integer songId);

    Optional<PlaylistSong> findByPlaylistIdAndSongId(Integer playlistId, Integer songId);
}