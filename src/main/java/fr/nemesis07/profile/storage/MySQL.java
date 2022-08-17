package fr.nemesis07.profile.storage;

import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {
    private Connection connection;

    private final String host;
    private final String port;
    private final String dbName;
    private final String username;
    private final String password;

    public MySQL(String host, String port, String dbName, String username, String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.username = username;
        this.password = password;
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://"+ host + ":" + port + "/" + dbName + "?characterEncoding=utf8",
                    username, password);
        }
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


    public void createTables() {
        update("CREATE TABLE IF NOT EXISTS Users (" +
                        "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(255), " +
                        "name VARCHAR(17), " +
                        "coins BIGINT, " +
                        "youtube VARCHAR(255), " +
                        "twitter VARCHAR(255), " +
                        "twitch VARCHAR(255), " +
                        "discord VARCHAR(255), "+
                        "createDate DATETIME, "+
                        "playTime BIGINT)");
    }

    public void update(String qry){
        try (PreparedStatement s = getConnection().prepareStatement(qry)) {
            s.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Object query(String qry, Function<ResultSet, Object> consumer){
        try (PreparedStatement s = getConnection().prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {
            return consumer.apply(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void query(String qry, Consumer<ResultSet> consumer){
        try (PreparedStatement s = getConnection().prepareStatement(qry);
            ResultSet rs = s.executeQuery()) {
            consumer.accept(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}