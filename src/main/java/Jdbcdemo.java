import java.sql.*;

public class Jdbcdemo {

    public static void main(String[] args) {
        System.out.println("JDBC Demo!");

        selectAllDemo();
        insertStudentDemo("Name des Studenten", "Email@prov.at");
        selectAllDemo();
        updateStudentDemo("Neuer Name", "neueemail@provider.at",1);
        selectAllDemo();
        deleteStudentDemo(4);
        selectAllDemo();

    }


    public static void deleteStudentDemo(int studentId) {
        System.out.println("DELETE Demo mit JDBC");

        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        //Verbindung zur DB wird hergestellt mit einem Connection Object welchem die URL, user und pw mitgegeben werden.
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, pwd)) {

            System.out.println("Verbindung zur DB hergestellt!");

            //Diesmal wird ein Parameter übergeben
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE student.id = ?"
            );
            try {
                ps.setInt(1,studentId);
                int rowAffected = ps.executeUpdate();
                System.out.println("Datensätze gelöscht = " + rowAffected);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-DELETE-Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler : " + e.getMessage());
        }
    }


    public static void updateStudentDemo(String neuerName, String neueEmail, int id){
        System.out.println("UPDATE Demo mit JDBC");

        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        //Verbindung zur DB wird hergestellt mit einem Connection Object welchem die URL, user und pw mitgegeben werden.
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, pwd)) {

            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement ps = conn.prepareStatement("UPDATE `student` SET `name` = ?, `email` = ? WHERE `student`.`id` = ?"
            );
            try {
                ps.setString(1,neuerName);
                ps.setString(2, neueEmail);
                ps.setInt(3,id);
                int affectedRows = ps.executeUpdate();
                System.out.println("Datensätze verändert: " + affectedRows);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-UPDATE-Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler : " + e.getMessage());
        }

    }


    public static void insertStudentDemo(String name, String email) {

        System.out.println("INSERT Demo mit JDBC");

        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        //Verbindung zur DB wird hergestellt mit einem Connection Object welchem die URL, user und pw mitgegeben werden.
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, pwd)) {

            System.out.println("Verbindung zur DB hergestellt!");

            //Beim eigentlichen Aufruf werden nurnoch die fertigen Datenwerte übergeben. -> Vorbeugung von manuellen SQL-Statements vom User
            PreparedStatement ps = conn.prepareStatement("INSERT INTO `student` (`id`, `name`, `email`) VALUES (NULL, ?, ?)"
            );
            //fertige Datenwerte die dann übergeben werden
            try {
                ps.setString(1, name);
                ps.setString(2, email);

                int rowAffected = ps.executeUpdate();
                System.out.println(rowAffected + "Datensätze eingefügt");

            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-INSERT-Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler : " + e.getMessage());
        }


    }


    public static void selectAllDemo() {

        System.out.println("Select Demo mit JDBC");
        String sqlSelectAllPerson = " SELECT * FROM `student` ";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        //Verbindung zur DB wird hergestellt mit einem Connection Object welchem die URL, user und pw mitgegeben werden.
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, pwd)) {
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement ps = conn.prepareStatement(sqlSelectAllPerson);
            ResultSet rs = ps.executeQuery();
            //Solange rs.next() nicht false zurück gibt (pointer auf das nächste Objekt) wird die while-Schleife ausgeführt
            while (rs.next()) {
                //Gibt die Daten der angegebenen Spalte zurück.
                long id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("Student aus der DB: ID: " + id + " NAME: " + name + " EMAIL: " + email);

            }

        } catch (SQLException e) {
            System.out.println("Fehler : " + e.getMessage());
        }

    }

}
