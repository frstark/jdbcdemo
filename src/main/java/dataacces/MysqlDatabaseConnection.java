package dataacces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class MysqlDatabaseConnection {
    private static Connection con = null;

    private MysqlDatabaseConnection(){

    }

    //Verbindung zur Datenbank herstellen
    public static Connection getConnection(String url, String user, String pwd) throws ClassNotFoundException, SQLException {


        if(con!=null) {
            return con;
        }
        //Liefert mir bei bestehender Verbindung, diese auch wieder zurück
        Class.forName("com.mysql.cj.jdbc.Driver");
        con =  DriverManager.getConnection(url,user,pwd);
        return con;
    }
}
