package organizer.data.base.connection;

import org.telegram.telegrambots.meta.logging.BotLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private final static String DB_URL = "jdbc:postgresql://localhost:9873/organizerdb";
    private final static String LOGIN = "postgres";
    private final static String PASSWORD = "1933420 ";

    private java.sql.Connection connection;

    static {
        DriverManager.setLoginTimeout(10);
    }

    public Connection() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        connection.setAutoCommit(true);
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    /**
     * Закрывает соединение
     * @throws SQLException
     */
    public void close(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            BotLogger.error(this.getClass().getName(), throwables.getMessage(), throwables);
        }
    }
}
