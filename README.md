# Playlist API

A Spring Boot REST API for managing music playlists, songs, and users. Built for easy playlist creation and music organization.

## 🚀 Quick Start

1. **Prerequisites**: Java 17+ and Gradle
2. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```
3. **View API documentation**: Open `http://localhost:8080/swagger-ui.html`

## 📋 Features

- **User Management**: Create and manage user accounts
- **Song Library**: Add, update, and organize your music collection
- **Playlist Creation**: Build custom playlists and manage their contents
- **Easy Integration**: RESTful API with comprehensive Swagger documentation

## 🔗 API Endpoints

### Users
- `GET /api/user/{id}` - Get user by ID
- `PUT /api/user/{id}` - Update user information
- `DELETE /api/user/{id}` - Delete a user
- `POST /api/user/add-user/{userName}` - Create a new user
- `GET /api/user/all-users` - List all users

### Songs
- `GET /api/song/{id}` - Get song by ID
- `PUT /api/song/{id}` - Update song information
- `DELETE /api/song/{id}` - Delete a song
- `POST /api/song/add-song` - Add a new song
- `GET /api/song/all-songs` - List all songs

### Playlists
- `PUT /api/playlist/{id}` - Update playlist name
- `DELETE /api/playlist/{id}` - Delete a playlist
- `POST /api/playlist/add-song-to-playlist/{playlistId}/{songId}` - Add song to playlist
- `POST /api/playlist/add-playlist` - Create a new playlist
- `GET /api/playlist/playlist-songs/{id}` - Get all songs in a playlist
- `GET /api/playlist/all` - List all playlists
- `DELETE /api/playlist/{playlistId}/{songId}` - Remove song from playlist

## 💾 Database

Uses **HSQLDB** (in-memory) for development:
- No setup required
- Data resets on application restart
- Perfect for development and testing

### Data Model
- **Users** can create multiple **Playlists**
- **Playlists** can contain multiple **Songs**
- **Songs** can appear in multiple **Playlists**

## 🧪 Development

### Build the project:
```bash
./gradlew build
```

### Run tests:
```bash
./gradlew test
```

### Project Structure:
```
src/
├── main/java/
│   ├── controller/     # REST API endpoints
│   ├── service/        # Business logic
│   └── persistence/    # Database entities & repositories
├── main/resources/
│   ├── PlalistERD.png  # Database schema diagram
└── test/java/          # Unit and integration tests
```

*For detailed API specifications and interactive testing, visit the Swagger UI at `/swagger-ui.html` when the application is running.*