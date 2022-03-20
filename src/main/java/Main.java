import dataacces.MySqlCourseRepository;
import dataacces.MysqlDatabaseConnection;
import ui.CLI;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            CLI myCLI = new CLI(new MySqlCourseRepository());
            myCLI.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " +e.getSQLState());
        } catch (ClassNotFoundException e){
            System.out.println("Datenbankfehler: " + e.getMessage());

        }




    }
}
