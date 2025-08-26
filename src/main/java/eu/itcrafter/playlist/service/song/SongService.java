package eu.itcrafter.playlist.service.song;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.persistence.song.SongRepository;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static eu.itcrafter.playlist.utils.Error.SONG_ALREADY_EXISTS;
import static eu.itcrafter.playlist.utils.Error.SONG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public SongDto getSongById(Integer songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException(SONG_NOT_FOUND.getMessage()));
        return songMapper.toSongDto(song);
    }

    public void updateSong(Integer id, SongDto songDto) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SONG_NOT_FOUND.getMessage()));

        if (songRepository.existsByNameAndIdNot(songDto.getName(), id)) {
            throw new DatabaseConstraintException(SONG_ALREADY_EXISTS.getMessage());
        }

        updateSongFields(songDto, song);
    }

    private void updateSongFields(SongDto songDto, Song song) {
        song.setName(songDto.getName());
        song.setArtist(songDto.getArtist());
        song.setMood(songDto.getMood());
        String duration = songDto.getDuration();
        if (duration != null && !duration.trim().isEmpty()) {
            validateDurationFormat(duration);
            song.setDuration(duration);
        } else {
            throw new DatabaseConstraintException("Duration is required (e.g., '3:45')");
        }
        song.setGenre(songDto.getGenre());
        songRepository.save(song);
    }

    private void validateDurationFormat(String duration) {
        if (!duration.matches("^\\d{1,2}:\\d{2}$")) {
            throw new DatabaseConstraintException("Duration must be in format M:SS or MM:SS (e.g., '3:45')");
        }

        String[] time = duration.split(":");
        int minutes = Integer.parseInt(time[0]);
        int seconds = Integer.parseInt(time[1]);

        if (minutes > 60) {
            throw new DatabaseConstraintException("Duration cannot exceed 60 minutes");
        }

        if (seconds >= 60) {
            throw new DatabaseConstraintException("Seconds must be less than 60");
        }

        if (minutes == 0 && seconds == 0) {
            throw new DatabaseConstraintException("Duration must be greater than 0:00");
        }
    }

    public void addSong(SongDto songDto) {
        if (songRepository.existsByNameAndIdNot(songDto.getName(), -1)) {
            throw new DatabaseConstraintException(SONG_ALREADY_EXISTS.getMessage());
        }

        Song song = new Song();
        updateSongFields(songDto, song);
    }

    public void deleteSong(Integer id) {
        if (!songRepository.existsById(id)) {
            throw new ResourceNotFoundException(SONG_NOT_FOUND.getMessage());
        }
        songRepository.deleteById(id);
    }

}

