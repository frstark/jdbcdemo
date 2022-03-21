import dataacces.MySqlCourseRepository;
import dataacces.MySqlStudentRepository;
import dataacces.MysqlDatabaseConnection;
import ui.CLI;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            CLI myCLI = new CLI(new MySqlCourseRepository(),new MySqlStudentRepository());
            myCLI.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " +e.getSQLState());
        } catch (ClassNotFoundException e){
            System.out.println("Datenbankfehler: " + e.getMessage());

        }




    }
}
