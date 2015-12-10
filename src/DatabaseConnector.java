import java.sql.*;
import java.util.Properties;

/**
 * Created by joserran on 11/30/2015.
 */

public class DatabaseConnector
{
    Connection conn;
    Statement stat;

    static String url, database, username, password, hostname, port, driver;

    public DatabaseConnector()
    {
        database = "messenger";
        username = "root";
        password = "luis";
        hostname = "localhost";
        port = "3306";
        driver = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://"+hostname + ":"+port+"/"+database;
    }

    public boolean open()
    {
        try
        {
            DriverManager.registerDriver((java.sql.Driver) Class.forName(driver).newInstance());
            conn = DriverManager.getConnection(url, username, password);
            stat = conn.createStatement();
        }
        catch (Exception e)
        {
            System.out.print("Connection unsuccessful:");
            e.printStackTrace();
            if(conn == null)
                return false;
        }

        return true;
    }

    public void close()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String s) throws SQLException
    {
        return stat.executeQuery(s);
    }

    public void executeUpdate(String s) throws SQLException
    {
        stat.executeUpdate(s);
    }
}
