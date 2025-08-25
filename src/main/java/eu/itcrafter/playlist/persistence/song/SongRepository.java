package eu.itcrafter.playlist.persistence.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    boolean existsByNameAndIdNot(String name, Integer id);
}