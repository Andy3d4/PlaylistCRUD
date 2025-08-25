package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.persistence.song.SongRepository;
import eu.itcrafter.playlist.service.song.SongMapper;
import eu.itcrafter.playlist.service.song.SongService;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private SongMapper songMapper;

    @InjectMocks
    private SongService songService;

    private Song testSong;
    private SongDto testSongDto;

    @BeforeEach
    void setUp() {
        testSong = new Song();
        testSong.setId(1);
        testSong.setName("Test Song");
        testSong.setArtist("Test Artist");
        testSong.setMood("Happy");
        testSong.setDuration("3:45");
        testSong.setGenre("Pop");

        testSongDto = new SongDto();
        testSongDto.setId(1);
        testSongDto.setName("Test Song");
        testSongDto.setArtist("Test Artist");
        testSongDto.setMood("Happy");
        testSongDto.setDuration("3:45");
        testSongDto.setGenre("Pop");
    }

    @Test
    void getAllSongs_ShouldReturnAllSongs_WhenSongsExist() {
        // Given
        List<Song> songs = List.of(testSong);

        when(songRepository.findAll()).thenReturn(songs);

        // When
        List<Song> result = songService.getAllSongs();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSong, result.get(0));

        verify(songRepository).findAll();
    }

    @Test
    void getAllSongs_ShouldReturnEmptyList_WhenNoSongsExist() {
        // Given
        List<Song> emptyList = new ArrayList<>();

        when(songRepository.findAll()).thenReturn(emptyList);

        // When
        List<Song> result = songService.getAllSongs();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(songRepository).findAll();
    }

    @Test
    void getSongById_ShouldReturnSongDto_WhenSongExists() {
        // Given
        Integer songId = 1;

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songMapper.toSongDto(testSong)).thenReturn(testSongDto);

        // When
        SongDto result = songService.getSongById(songId);

        // Then
        assertNotNull(result);
        assertEquals(testSongDto, result);

        verify(songRepository).findById(songId);
        verify(songMapper).toSongDto(testSong);
    }

    @Test
    void getSongById_ShouldThrowResourceNotFoundException_WhenSongDoesNotExist() {
        // Given
        Integer songId = 999;

        when(songRepository.findById(songId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> songService.getSongById(songId)
        );

        assertNotNull(exception.getMessage());

        verify(songRepository).findById(songId);
        verifyNoInteractions(songMapper);
    }

    @Test
    void updateSong_ShouldUpdateSong_WhenValidDataProvided() {
        // Given
        Integer songId = 1;
        testSongDto.setName("Updated Song");
        testSongDto.setArtist("Updated Artist");

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), songId)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When
        songService.updateSong(songId, testSongDto);

        // Then
        assertEquals("Updated Song", testSong.getName());
        assertEquals("Updated Artist", testSong.getArtist());
        assertEquals(testSongDto.getMood(), testSong.getMood());
        assertEquals(testSongDto.getDuration(), testSong.getDuration());
        assertEquals(testSongDto.getGenre(), testSong.getGenre());

        verify(songRepository).findById(songId);
        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), songId);
        verify(songRepository).save(testSong);
    }

    @Test
    void updateSong_ShouldThrowResourceNotFoundException_WhenSongDoesNotExist() {
        // Given
        Integer songId = 999;

        when(songRepository.findById(songId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> songService.updateSong(songId, testSongDto)
        );

        assertNotNull(exception.getMessage());

        verify(songRepository).findById(songId);
        verify(songRepository, never()).save(any());
    }

    @Test
    void updateSong_ShouldThrowDatabaseConstraintException_WhenSongNameAlreadyExists() {
        // Given
        Integer songId = 1;
        testSongDto.setName("Existing Song Name");

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), songId)).thenReturn(true);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.updateSong(songId, testSongDto)
        );

        assertNotNull(exception.getMessage());

        verify(songRepository).findById(songId);
        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), songId);
        verify(songRepository, never()).save(any());
    }

    @Test
    void updateSong_ShouldThrowDatabaseConstraintException_WhenDurationIsNull() {
        // Given
        Integer songId = 1;
        testSongDto.setDuration(null);

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), songId)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.updateSong(songId, testSongDto)
        );

        assertEquals("Duration is required (e.g., '3:45')", exception.getMessage());

        verify(songRepository).findById(songId);
        verify(songRepository, never()).save(any());
    }

    @Test
    void updateSong_ShouldThrowDatabaseConstraintException_WhenDurationIsEmpty() {
        // Given
        Integer songId = 1;
        testSongDto.setDuration("  ");

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), songId)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.updateSong(songId, testSongDto)
        );

        assertEquals("Duration is required (e.g., '3:45')", exception.getMessage());

        verify(songRepository).findById(songId);
        verify(songRepository, never()).save(any());
    }

    @Test
    void addSong_ShouldCreateSong_WhenValidDataProvided() {
        // Given
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When
        songService.addSong(testSongDto);

        // Then
        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository).save(any(Song.class));
    }

    @Test
    void addSong_ShouldThrowDatabaseConstraintException_WhenSongNameAlreadyExists() {
        // Given
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(true);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertNotNull(exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void addSong_ShouldThrowDatabaseConstraintException_WhenDurationIsNull() {
        // Given
        testSongDto.setDuration(null);

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration is required (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void addSong_ShouldThrowDatabaseConstraintException_WhenDurationIsEmpty() {
        // Given
        testSongDto.setDuration("   ");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration is required (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void deleteSong_ShouldDeleteSong_WhenSongExists() {
        // Given
        Integer songId = 1;

        when(songRepository.existsById(songId)).thenReturn(true);

        // When
        songService.deleteSong(songId);

        // Then
        verify(songRepository).existsById(songId);
        verify(songRepository).deleteById(songId);
    }

    @Test
    void deleteSong_ShouldThrowResourceNotFoundException_WhenSongDoesNotExist() {
        // Given
        Integer songId = 999;

        when(songRepository.existsById(songId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> songService.deleteSong(songId)
        );

        assertNotNull(exception.getMessage());

        verify(songRepository).existsById(songId);
        verify(songRepository, never()).deleteById(anyInt());
    }

    // Duration Validation Tests

    @Test
    void validateDurationFormat_ShouldAcceptValidFormats() {
        // Given
        testSongDto.setDuration("3:45");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.addSong(testSongDto));

        verify(songRepository).save(any(Song.class));
    }

    @Test
    void validateDurationFormat_ShouldAcceptSingleDigitMinutes() {
        // Given
        testSongDto.setDuration("5:30");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.addSong(testSongDto));

        verify(songRepository).save(any(Song.class));
    }

    @Test
    void validateDurationFormat_ShouldAcceptDoubleDigitMinutes() {
        // Given
        testSongDto.setDuration("45:30");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.addSong(testSongDto));

        verify(songRepository).save(any(Song.class));
    }

    @Test
    void validateDurationFormat_ShouldRejectInvalidFormat() {
        // Given
        testSongDto.setDuration("3:456");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration must be in format M:SS or MM:SS (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldRejectNoColon() {
        // Given
        testSongDto.setDuration("345");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration must be in format M:SS or MM:SS (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldRejectMinutesOver60() {
        // Given
        testSongDto.setDuration("61:30");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration cannot exceed 60 minutes", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldRejectSecondsOver59() {
        // Given
        testSongDto.setDuration("3:60");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Seconds must be less than 60", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldRejectZeroDuration() {
        // Given
        testSongDto.setDuration("0:00");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration must be greater than 0:00", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldAcceptMinimalValidDuration() {
        // Given
        testSongDto.setDuration("0:01");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.addSong(testSongDto));

        verify(songRepository).save(any(Song.class));
    }

    @Test
    void validateDurationFormat_ShouldAcceptMaxValidDuration() {
        // Given
        testSongDto.setDuration("60:59");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.addSong(testSongDto));

        verify(songRepository).save(any(Song.class));
    }

    @Test
    void validateDurationFormat_ShouldRejectInvalidCharacters() {
        // Given
        testSongDto.setDuration("3:4a");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration must be in format M:SS or MM:SS (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void validateDurationFormat_ShouldRejectNegativeValues() {
        // Given
        testSongDto.setDuration("-3:45");

        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), -1)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> songService.addSong(testSongDto)
        );

        assertEquals("Duration must be in format M:SS or MM:SS (e.g., '3:45')", exception.getMessage());

        verify(songRepository).existsByNameAndIdNot(testSongDto.getName(), -1);
        verify(songRepository, never()).save(any());
    }

    @Test
    void updateSong_ShouldAcceptValidDurationFormats() {
        // Given
        Integer songId = 1;
        testSongDto.setDuration("12:34");

        when(songRepository.findById(songId)).thenReturn(Optional.of(testSong));
        when(songRepository.existsByNameAndIdNot(testSongDto.getName(), songId)).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> songService.updateSong(songId, testSongDto));

        assertEquals("12:34", testSong.getDuration());
        verify(songRepository).save(testSong);
    }
}