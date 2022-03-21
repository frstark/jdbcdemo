package dataacces;

import domain.Course;
import domain.CourseType;
import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository {


    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {

        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");

    }

    private int countStudentenInDbWithId(Long id) {

        try {
            String countSql = "SELECT COUNT(*) FROM `studenten` WHERE `id`=?";
            PreparedStatement prepareStatementCount = con.prepareStatement(countSql);
            prepareStatementCount.setLong(1, id);
            ResultSet resultSetCount = prepareStatementCount.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }


    }


    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `studenten` (`vorname`, `nachname`, `birthdate`) VALUES (?,?,?)";
            PreparedStatement prepareStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, entity.getVorname());
            prepareStatement.setString(2, entity.getNachname());
            prepareStatement.setDate(3, entity.getBirthDate());

            int affectedRows = prepareStatement.executeUpdate();


            if (affectedRows == 0) {
                return Optional.empty();
            }
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));

            } else {
                return Optional.empty();
            }


        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());

        }


    }

    @Override
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if (countStudentenInDbWithId(id) == 0) {
            return Optional.empty();
        } else {

            try {
                String sql = "SELECT * FROM `studenten` WHERE `id` = ?";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setLong(1, id);
                ResultSet resultSet = prepareStatement.executeQuery();

                resultSet.next();
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getDate("birthdate")

                );
                return Optional.of(student);

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }

        }


    }


    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `studenten`";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                studentList.add(new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("vorname"),
                                resultSet.getString("nachname"),
                                resultSet.getDate("birthdate")

                        )

                );

            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }


    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Student> findStudentByName(String name) {

            try {
                String sql = "SELECT * FROM `studenten` WHERE LOWER(`vorname`) LIKE LOWER(?) OR LOWER(`nachname`) LIKE LOWER(?)";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, "%" + name + "%");
                prepareStatement.setString(2, "%" + name + "%");

                ResultSet resultSet = prepareStatement.executeQuery();

                ArrayList<Student> studentList = new ArrayList<>();
                while (resultSet.next()) {
                    studentList.add(new Student(
                                    resultSet.getLong("id"),
                                    resultSet.getString("vorname"),
                                    resultSet.getString("nachname"),
                                    resultSet.getDate("birthdate")
                            )
                    );
                }
                return studentList;


            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }


        }


    @Override
    public List<Student> findStudentByBirthDate(String dateSearch) {

            try {
                String sql = "SELECT * FROM `studenten` WHERE `birthdate` LIKE ? ";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, "%" + dateSearch + "%");

                ResultSet resultSet = prepareStatement.executeQuery();
                ArrayList<Student> studentList = new ArrayList<>();
                while (resultSet.next()) {
                    studentList.add(new Student(
                                    resultSet.getLong("id"),
                                    resultSet.getString("vorname"),
                                    resultSet.getString("nachname"),
                                    resultSet.getDate("birthdate")
                            )
                    );
                }
                return studentList;


            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }


        }

        @Override
        public List<Student> findStudentByBirthDatePeriod (Date startDate, Date endDate){
            return null;
        }

    }

