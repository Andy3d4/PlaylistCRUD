package eu.itcrafter.playlist.infrastructure.db;

import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HsqlServerConfig {
    private static final Logger log = LoggerFactory.getLogger(HsqlServerConfig.class);

    /**
     * This bean starts an HSQLDB Server on port 9001, backed by an in-memory database named "mydb".
     * IntelliJ can then connect to jdbc:hsqldb:hsql://localhost:9001/mydb.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server hsqlServer() {
        Server server = new Server();
        server.setLogWriter(null);      // suppress console logging from HSQLDB itself
        server.setSilent(true);         // reduce verbosity
        server.setDatabaseName(0, "mydb");       // this name must match “mem:mydb”
        server.setDatabasePath(0, "mem:mydb");   // in-memory mode
        server.setPort(9001);                   // TCP port for client connections
        log.info("Starting HSQLDB server on port 9001 (in-memory database 'mydb')");
        return server;
    }
}
