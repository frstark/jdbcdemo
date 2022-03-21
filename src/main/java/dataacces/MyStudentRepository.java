package dataacces;

import domain.Student;

import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student,Long> {


    List<Student> findStudentByName(String name);
    List<Student> findStudentByBirthDate(String dateSearch);
    List<Student> findStudentByBirthDatePeriod(Date startDate, Date endDate);




}
