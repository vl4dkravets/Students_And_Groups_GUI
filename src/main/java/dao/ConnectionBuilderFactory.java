package dao;


/**
 * Class returns DAO implementation
 * Presents a way to replace data access realization
 */
public class ConnectionBuilderFactory
{
    public static ConnectionBuilder getConnectionBuilder() {
        return new SimpleConnectionBuilder();
    }
}
