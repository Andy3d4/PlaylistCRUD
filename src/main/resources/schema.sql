-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-08-12 15:30:09.353

-- tables
-- Table: Playlist
CREATE TABLE Playlist (
    id int GENERATED ALWAYS AS IDENTITY NOT NULL,
    userId int  NOT NULL,
    name varchar(20)  NOT NULL,
    createdAt date  NOT NULL,
    CONSTRAINT Playlist_pk PRIMARY KEY (id)
);

-- Table: PlaylistSong
CREATE TABLE PlaylistSong (
    playlistId int  NOT NULL,
    songId int  NOT NULL,
    CONSTRAINT PlaylistSong_pk PRIMARY KEY (playlistId,songId)
);

-- Table: Song
CREATE TABLE Song (
    id int GENERATED ALWAYS AS IDENTITY NOT NULL,
    name varchar(50)  NOT NULL,
    mood varchar(12)  NOT NULL,
    duration varchar(12)  NOT NULL,
    artist varchar(20)  NOT NULL,
    genre varchar(12)  NOT NULL,
    CONSTRAINT Song_pk PRIMARY KEY (id)
);

-- Table: User
CREATE TABLE "User" (
    id int GENERATED ALWAYS AS IDENTITY NOT NULL,
    username varchar(12)  NOT NULL,
    CONSTRAINT User_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: PlaylistSong_Playlist (table: PlaylistSong)
ALTER TABLE PlaylistSong ADD CONSTRAINT PlaylistSong_Playlist
    FOREIGN KEY (playlistId)
    REFERENCES Playlist (id);

-- Reference: PlaylistSong_Song (table: PlaylistSong)
ALTER TABLE PlaylistSong ADD CONSTRAINT PlaylistSong_Song
    FOREIGN KEY (songId)
    REFERENCES Song (id);

-- Reference: Playlist_User (table: Playlist)
ALTER TABLE Playlist ADD CONSTRAINT Playlist_User
    FOREIGN KEY (userId)
    REFERENCES "User" (id);

-- End of file.

