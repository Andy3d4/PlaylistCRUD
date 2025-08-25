package eu.itcrafter.playlist.service.playlist;

import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.persistence.playlist.PlaylistRepository;
import eu.itcrafter.playlist.persistence.playlistsong.PlaylistSong;
import eu.itcrafter.playlist.persistence.playlistsong.PlaylistSongRepository;
import eu.itcrafter.playlist.persistence.user.User;
import eu.itcrafter.playlist.persistence.user.UserRepository;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaylistMapper playlistMapper;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistSongRepository playlistSongRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private User testUser;
    private Playlist testPlaylist;
    private PlaylistDto testPlaylistDto;
    private PlaylistWithSongsDto testPlaylistWithSongsDto;
    private PlaylistSong testPlaylistSong;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("Andy");

        testPlaylist = new Playlist();
        testPlaylist.setId(1);
        testPlaylist.setName("SadPlaylist");
        testPlaylist.setCreatedat(LocalDate.of(2025, 8, 6));
        testPlaylist.setUser(testUser);

        testPlaylistDto = new PlaylistDto();
        testPlaylistDto.setId(1);
        testPlaylistDto.setName("SadPlaylist");
        testPlaylistDto.setCreatedat(LocalDate.of(2025, 8, 6));
        testPlaylistDto.setUsername("Andy");

        testPlaylistWithSongsDto = new PlaylistWithSongsDto();
        testPlaylistWithSongsDto.setId(1);
        testPlaylistWithSongsDto.setName("SadPlaylist");

        testPlaylistSong = new PlaylistSong(1, 1);
    }

    @Test
    void getAllPlaylists_ShouldReturnAllPlaylists_WhenPlaylistsExist() {
        // Given
        List<Playlist> playlists = List.of(testPlaylist);
        List<PlaylistDto> expectedDtos = List.of(testPlaylistDto);

        when(playlistRepository.findAll()).thenReturn(playlists);
        when(playlistMapper.toPlaylistDtoList(playlists)).thenReturn(expectedDtos);

        // When
        List<PlaylistDto> result = playlistService.getAllPlaylists();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDtos, result);

        verify(playlistRepository).findAll();
        verify(playlistMapper).toPlaylistDtoList(playlists);
    }

    @Test
    void getAllPlaylists_ShouldReturnEmptyList_WhenNoPlaylistsExist() {
        // Given
        List<Playlist> emptyList = new ArrayList<>();
        List<PlaylistDto> emptyDtoList = new ArrayList<>();

        when(playlistRepository.findAll()).thenReturn(emptyList);
        when(playlistMapper.toPlaylistDtoList(emptyList)).thenReturn(emptyDtoList);

        // When
        List<PlaylistDto> result = playlistService.getAllPlaylists();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(playlistRepository).findAll();
        verify(playlistMapper).toPlaylistDtoList(emptyList);
    }

    @Test
    void getAllSongsInPlaylist_ShouldReturnPlaylistWithSongs_WhenPlaylistExists() {
        // Given
        int playlistId = 1;

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(testPlaylist));
        when(playlistMapper.toPlaylistWithSongsDto(testPlaylist)).thenReturn(testPlaylistWithSongsDto);

        // When
        PlaylistWithSongsDto result = playlistService.getAllSongsInPlaylist(playlistId);

        // Then
        assertNotNull(result);
        assertEquals(testPlaylistWithSongsDto, result);

        verify(playlistRepository).findById(playlistId);
        verify(playlistMapper).toPlaylistWithSongsDto(testPlaylist);
    }

    @Test
    void getAllSongsInPlaylist_ShouldThrowResourceNotFoundException_WhenPlaylistDoesNotExist() {
        // Given
        int playlistId = 999;

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> playlistService.getAllSongsInPlaylist(playlistId)
        );

        assertTrue(exception.getMessage().contains("PLAYLIST_NOT_FOUND") ||
                exception.getMessage().contains("Playlist not found"));

        verify(playlistRepository).findById(playlistId);
        verifyNoInteractions(playlistMapper);
    }

    @Test
    void updatePlaylistName_ShouldUpdatePlaylistName_WhenPlaylistExists() {
        // Given
        int playlistId = 1;
        String newName = "UpdatedPlaylist";

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(testPlaylist));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(testPlaylist);

        // When
        playlistService.updatePlaylistName(playlistId, newName);

        // Then
        assertEquals(newName, testPlaylist.getName());

        verify(playlistRepository).findById(playlistId);
        verify(playlistRepository).save(testPlaylist);
    }

    @Test
    void updatePlaylistName_ShouldThrowResourceNotFoundException_WhenPlaylistDoesNotExist() {
        // Given
        int playlistId = 999;
        String newName = "UpdatedPlaylist";

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> playlistService.updatePlaylistName(playlistId, newName)
        );

        assertTrue(exception.getMessage().contains("PLAYLIST_NOT_FOUND") ||
                exception.getMessage().contains("Playlist not found"));

        verify(playlistRepository).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    void addPlaylist_ShouldCreatePlaylist_WhenValidDataProvided() {
        // Given
        when(userRepository.findByUsername(testPlaylistDto.getUsername())).thenReturn(Optional.of(testUser));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(testPlaylist);

        // When
        playlistService.addPlaylist(testPlaylistDto);

        // Then
        verify(userRepository).findByUsername(testPlaylistDto.getUsername());
        verify(playlistRepository).save(any(Playlist.class));
    }

    @Test
    void addPlaylist_ShouldThrowIllegalArgumentException_WhenUsernameIsNull() {
        // Given
        testPlaylistDto.setUsername(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> playlistService.addPlaylist(testPlaylistDto)
        );

        assertEquals("Username cannot be null or empty", exception.getMessage());

        verifyNoInteractions(userRepository);
        verifyNoInteractions(playlistRepository);
    }

    @Test
    void addPlaylist_ShouldThrowIllegalArgumentException_WhenUsernameIsEmpty() {
        // Given
        testPlaylistDto.setUsername("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> playlistService.addPlaylist(testPlaylistDto)
        );

        assertEquals("Username cannot be null or empty", exception.getMessage());

        verifyNoInteractions(userRepository);
        verifyNoInteractions(playlistRepository);
    }

    @Test
    void addPlaylist_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByUsername(testPlaylistDto.getUsername())).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> playlistService.addPlaylist(testPlaylistDto)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByUsername(testPlaylistDto.getUsername());
        verifyNoInteractions(playlistRepository);
    }

    @Test
    void addSongToPlaylist_ShouldAddSong_WhenValidDataProvided() {
        // Given
        Integer playlistId = 1;
        Integer songId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(true);
        when(playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)).thenReturn(false);
        when(playlistSongRepository.save(any(PlaylistSong.class))).thenReturn(testPlaylistSong);

        // When
        playlistService.addSongToPlaylist(playlistId, songId);

        // Then
        verify(playlistRepository).existsById(playlistId);
        verify(playlistSongRepository).existsByPlaylistIdAndSongId(playlistId, songId);
        verify(playlistSongRepository).save(any(PlaylistSong.class));
    }

    @Test
    void addSongToPlaylist_ShouldThrowDatabaseConstraintException_WhenPlaylistDoesNotExist() {
        // Given
        Integer playlistId = 999;
        Integer songId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> playlistService.addSongToPlaylist(playlistId, songId)
        );

        assertTrue(exception.getMessage().contains("PLAYLIST_NOT_FOUND") ||
                exception.getMessage().contains("Playlist not found"));

        verify(playlistRepository).existsById(playlistId);
        verifyNoInteractions(playlistSongRepository);
    }

    @Test
    void addSongToPlaylist_ShouldThrowDatabaseConstraintException_WhenSongAlreadyExistsInPlaylist() {
        // Given
        Integer playlistId = 1;
        Integer songId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(true);
        when(playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)).thenReturn(true);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> playlistService.addSongToPlaylist(playlistId, songId)
        );

        assertTrue(exception.getMessage().contains("SONG_ALREADY_EXISTS") ||
                exception.getMessage().contains("Song already exists"));

        verify(playlistRepository).existsById(playlistId);
        verify(playlistSongRepository).existsByPlaylistIdAndSongId(playlistId, songId);
        verify(playlistSongRepository, never()).save(any());
    }

    @Test
    void deletePlaylist_ShouldDeletePlaylist_WhenPlaylistExists() {
        // Given
        int playlistId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(true);

        // When
        playlistService.deletePlaylist(playlistId);

        // Then
        verify(playlistRepository).existsById(playlistId);
        verify(playlistRepository).deleteById(playlistId);
    }

    @Test
    void deletePlaylist_ShouldThrowResourceNotFoundException_WhenPlaylistDoesNotExist() {
        // Given
        int playlistId = 999;

        when(playlistRepository.existsById(playlistId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> playlistService.deletePlaylist(playlistId)
        );

        assertTrue(exception.getMessage().contains("PLAYLIST_NOT_FOUND") ||
                exception.getMessage().contains("Playlist not found"));

        verify(playlistRepository).existsById(playlistId);
        verify(playlistRepository, never()).deleteById(anyInt());
    }

    @Test
    void deleteSongFromPlaylist_ShouldDeleteSong_WhenPlaylistSongExists() {
        // Given
        int playlistId = 1;
        int songId = 1;

        when(playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId))
                .thenReturn(Optional.of(testPlaylistSong));

        // When
        playlistService.deleteSongFromPlaylist(playlistId, songId);

        // Then
        verify(playlistSongRepository).findByPlaylistIdAndSongId(playlistId, songId);
        verify(playlistSongRepository).delete(testPlaylistSong);
    }

    @Test
    void deleteSongFromPlaylist_ShouldThrowResourceNotFoundException_WhenPlaylistSongDoesNotExist() {
        // Given
        int playlistId = 999;
        int songId = 999;

        when(playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId))
                .thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> playlistService.deleteSongFromPlaylist(playlistId, songId)
        );

        assertEquals("Song or Playlist not found", exception.getMessage());

        verify(playlistSongRepository).findByPlaylistIdAndSongId(playlistId, songId);
        verify(playlistSongRepository, never()).delete(any());
    }

    @Test
    void addSongToPlaylist_ShouldHandleNullPlaylistId() {
        // Given
        Integer playlistId = null;
        Integer songId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(false);

        // When & Then
        DatabaseConstraintException exception = assertThrows(
                DatabaseConstraintException.class,
                () -> playlistService.addSongToPlaylist(playlistId, songId)
        );

        verify(playlistRepository).existsById(playlistId);
    }

    @Test
    void addSongToPlaylist_ShouldHandleNullSongId() {
        // Given
        Integer playlistId = 1;
        Integer songId = null;

        when(playlistRepository.existsById(playlistId)).thenReturn(true);
        when(playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)).thenReturn(false);

        // When
        playlistService.addSongToPlaylist(playlistId, songId);

        // Then
        verify(playlistRepository).existsById(playlistId);
        verify(playlistSongRepository).existsByPlaylistIdAndSongId(playlistId, songId);
        verify(playlistSongRepository).save(any(PlaylistSong.class));
    }
}