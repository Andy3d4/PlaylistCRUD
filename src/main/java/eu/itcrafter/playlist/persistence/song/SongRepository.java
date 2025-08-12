package eu.itcrafter.playlist.persistence.song;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface SongRepository extends Repository<Song, Integer> {
    List<Song> findAll();
}