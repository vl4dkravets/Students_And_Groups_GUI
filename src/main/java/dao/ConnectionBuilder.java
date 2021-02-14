package dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Retrieves data from config & returns a connection to DB
 */
public interface ConnectionBuilder
{
    Connection getConnection() throws SQLException;
}
