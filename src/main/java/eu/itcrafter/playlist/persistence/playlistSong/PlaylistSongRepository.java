package eu.itcrafter.playlist.persistence.playlistSong;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Integer> {
    boolean existsByPlaylistIdAndSongId(Integer playlistId, Integer songId);
}