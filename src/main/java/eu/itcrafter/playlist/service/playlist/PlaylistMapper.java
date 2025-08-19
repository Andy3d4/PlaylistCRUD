package eu.itcrafter.playlist.service.playlist;


import eu.itcrafter.playlist.persistence.playlist.Playlist;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistDto;
import eu.itcrafter.playlist.service.playlist.dto.PlaylistWithSongsDto;
import eu.itcrafter.playlist.service.song.SongMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = SongMapper.class)
public interface PlaylistMapper {
    PlaylistWithSongsDto toPlaylistWithSongsDto(Playlist playlist);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "createdat", target = "createdat")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.username", target = "username")
    PlaylistDto toPlaylistDto(Playlist playlist);

    List<PlaylistDto> toPlaylistDtoList(List<Playlist> playlists);

}