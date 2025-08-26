Playlist Graduation Project
Overview
This is a Spring Boot REST API for managing playlists, songs, and users. It supports CRUD operations for playlists, songs, and users, and allows associating songs with playlists. The application uses an in-memory HSQLDB database for development and testing.
Features
User Management: Add, update, delete, and list users.
Playlist Management: Add, update, delete, and list playlists. Associate playlists with users.
Song Management: Add, update, delete, and list songs. Associate songs with playlists.
Playlist-Song Association: Add or remove songs from playlists.
OpenAPI/Swagger: API documentation via Swagger annotations.
Database
HSQLDB (In-Memory): No setup required; data is lost on application shutdown.
Schema: See src/main/resources/PlaylistERD.png for the entity relationship diagram.
Running the Application
Dependencies: Ensure you have Java 17+ and Gradle installed.
Start the App:
./gradlew bootRun
API Documentation: Visit /swagger-ui.html after starting the app.
Testing
Unit & Integration Tests: Located in src/test/java.
Test Database: Uses HSQLDB in-memory. Configuration is in src/test/resources/application.properties.
Run Tests:
./gradlew test
Configuration
Main DB Config: src/main/resources/application.properties
Test DB Config: src/test/resources/application.properties
spring.datasource.url=jdbc:hsqldb:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.hsqldb.jdbc.JDBCDriver
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
Project Structure
controller/ - REST controllers
service/ - Business logic
persistence/ - JPA entities and repositories
resources/ - Configuration, schema, and data files
Entity Relationship
User: Has many Playlists
Playlist: Belongs to a User, has many Songs (via PlaylistSong)
Song: Can belong to many Playlists
PlaylistSong: Join table for Playlist and Song
Useful Commands
Build: ./gradlew build
Run: ./gradlew bootRun
Test: ./gradlew test
License
MIT License
<hr></hr>
For more details, see the source code and API documentation.