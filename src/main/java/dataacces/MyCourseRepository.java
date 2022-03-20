package dataacces;

import domain.Course;
import domain.CourseType;

import java.sql.Date;
import java.util.List;


public interface MyCourseRepository  extends BaseRepository<Course,Long>{


    List<Course> findAllCourseByName(String name);

    List<Course> findAllCourseByDescription(String description);

    List<Course> findAllCourseByNameOrDescription(String searchText);

    List<Course> findAllCourseByCourseType(CourseType courseType);

    List<Course> findAllCourseByStartDate(Date startDate);

    List<Course> findAllRunningCourses();




}
