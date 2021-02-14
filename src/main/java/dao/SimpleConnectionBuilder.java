package dao;

import config.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionBuilder implements ConnectionBuilder
{
    public SimpleConnectionBuilder() {
        try {
            Class.forName(Config.getProperty("db.driver.class"));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        String url = Config.getProperty("db.url");
        String login = Config.getProperty("db.login");
        String password = Config.getProperty("db.password");
        return DriverManager.getConnection(url, login, password);
    }
}
