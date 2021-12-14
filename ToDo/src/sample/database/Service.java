package sample.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Service
{
    Connection connection=null;
    ServerSettings ss=null;

    public Service(Connection connection,ServerSettings ss)
    {
        this.connection=connection;
        this.ss=ss;
    }

    protected Connection getConnection() throws SQLException
    {
        if (connection == null)
        {
            String url = "jdbc:mysql://" + ss.hostname + ":"+ss.port+"/" + ss.databaseName;
            this.connection= DriverManager.getConnection(url, ss.username, ss.password);
        }
        return this.connection;
    }

}
