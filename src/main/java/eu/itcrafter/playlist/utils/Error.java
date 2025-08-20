package eu.itcrafter.playlist.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Error {

    SONG_NOT_FOUND("Song not found"),
    SONG_ALREADY_EXISTS("Song already exists"),

    PLAYLIST_NOT_FOUND("Playlist not found"),
    PLAYLIST_ALREADY_EXISTS("Playlist already exists");


    private final String message;


}
