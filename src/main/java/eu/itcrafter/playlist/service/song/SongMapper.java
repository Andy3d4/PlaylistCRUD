package eu.itcrafter.playlist.service.song;

import eu.itcrafter.playlist.persistence.song.Song;
import eu.itcrafter.playlist.service.song.dto.SongDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SongMapper {
    SongDto toDto(Song song);
    Song toSong(SongDto dto);
}