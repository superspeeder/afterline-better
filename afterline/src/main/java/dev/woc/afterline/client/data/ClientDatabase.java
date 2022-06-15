package dev.woc.afterline.client.data;

import dev.woc.afterline.client.Afterline;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;

public class ClientDatabase {

    private static String CONNECTION_PATH = "jdbc:sqlite:client.db";
    private static Path SIMPLEDATA_PATH = Path.of("client.properties");
    private Connection conn;
    private Properties simpleData;


    public ClientDatabase() {
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(CONNECTION_PATH);
            Afterline.LOGGER.info("Opened client database");
        } catch (SQLException e) {
            Afterline.LOGGER.fatal("Failed to open client database");
            Afterline.LOGGER.throwing(Level.FATAL, e);
            throw new RuntimeException(e);
        }

        simpleData = new Properties();
        File simpleDataFile = SIMPLEDATA_PATH.toFile();
        if (simpleDataFile.exists()) {
            try {
                simpleData.load(new FileReader(simpleDataFile));
            } catch (IOException e) {
                Afterline.LOGGER.error("Failed to load client simple data");
                Afterline.LOGGER.throwing(e);
            }
        }

        simpleData.setProperty("lastSave", String.valueOf(Instant.now().getEpochSecond()));

        try {
            simpleData.store(new FileWriter(simpleDataFile), "");
        } catch (IOException e) {
            Afterline.LOGGER.error("Failed to save client simple data");
            Afterline.LOGGER.throwing(e);
        }
    }

    public void close() {
        try {
            simpleData.setProperty("lastSave", String.valueOf(Instant.now().getEpochSecond()));
            simpleData.store(new FileWriter(SIMPLEDATA_PATH.toFile()), "");
        } catch (IOException e) {
            Afterline.LOGGER.error("Failed to save client simple data");
            Afterline.LOGGER.throwing(e);
        }

        if (conn == null) {
            Afterline.LOGGER.warn("Cannot close client database before it is opened");
            return;
        }

        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            Afterline.LOGGER.error("Failed to close client database");
            Afterline.LOGGER.throwing(e);
        }
    }
}
