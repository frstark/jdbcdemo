package dataacces;

import domain.Course;
import domain.CourseType;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlCourseRepository implements MyCourseRepository {


    private Connection con;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {

        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");

    }

    @Override
    public Optional<Course> insert(Course entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `courses` (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?)";
            PreparedStatement prepareStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, entity.getName());
            prepareStatement.setString(2, entity.getDescription());
            prepareStatement.setInt(3, entity.getHours());
            prepareStatement.setDate(4, entity.getBeginDate());
            prepareStatement.setDate(5, entity.getEndDate());
            prepareStatement.setString(6, entity.getCourseType().toString());

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
    public Optional<Course> getById(Long id) {
        Assert.notNull(id);
        if (countCoursesInDbWithId(id) == 0) {
            return Optional.empty();
        } else {

            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setLong(1, id);
                ResultSet resultSet = prepareStatement.executeQuery();

                resultSet.next();
                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );
                return Optional.of(course);

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }

        }


    }

    private int countCoursesInDbWithId(Long id) {

        try {
            String countSql = "SELECT COUNT(*) FROM `courses` WHERE `id`=?";
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
    public List<Course> getAll() {
        String sql = "SELECT * FROM `courses`";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                courseList.add(new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        )

                );


            }
            return courseList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }

    }

    @Override
    public Optional<Course> update(Course entity) {

        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";

        if (countCoursesInDbWithId(entity.getId()) == 0) {
            return Optional.empty();
        } else {

            try {
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, entity.getName());
                prepareStatement.setString(2, entity.getDescription());
                prepareStatement.setInt(3, entity.getHours());
                prepareStatement.setDate(4, entity.getBeginDate());
                prepareStatement.setDate(5, entity.getEndDate());
                prepareStatement.setString(6, entity.getCourseType().toString());
                prepareStatement.setLong(7, entity.getId());

                int affectedRows = prepareStatement.executeUpdate();

                if (affectedRows == 0) {
                    return Optional.empty();
                } else {
                    return this.getById(entity.getId());
                }

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }

        }

    }

    @Override
    public void deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `courses` WHERE `id` = ?";
        {

            try {
                if (countCoursesInDbWithId(id) == 1) {
                    PreparedStatement prepareStatement = con.prepareStatement(sql);
                    prepareStatement.setLong(1, id);
                    prepareStatement.executeUpdate();
                }
            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }

    }

    @Override
    public List<Course> findAllCourseByName(String name) {
        return null;
    }

    @Override
    public List<Course> findAllCourseByDescription(String description) {
        return null;
    }

    @Override
    public List<Course> findAllCourseByNameOrDescription(String searchText) {
        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?)";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            prepareStatement.setString(1, "%" + searchText + "%");
            prepareStatement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = prepareStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                courseList.add(new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );
            }
            return courseList;


        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }



    }

    @Override
    public List<Course> findAllCourseByCourseType(CourseType courseType) {
        return null;
    }

    @Override
    public List<Course> findAllCourseByStartDate(Date startDate) {
        return null;
    }

    @Override
    public List<Course> findAllRunningCourses() {
        String sql = "SELECT * FROM `courses` WHERE NOW()<`enddate`";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                courseList.add(new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseList;
        } catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }




    }
}
