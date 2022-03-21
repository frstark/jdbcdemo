package ui;

import dataacces.DatabaseException;
import dataacces.MyCourseRepository;
import dataacces.MyStudentRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;
import domain.Student;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLI {

    Scanner scanner;
    MyCourseRepository repo;
    MyStudentRepository stRepo;

    public CLI(MyCourseRepository repo, MyStudentRepository stRepo) {
        this.scanner = new Scanner(System.in);
        this.repo = repo;
        this.stRepo = stRepo;
    }

    public void start() {
        String input = "-";
        while (!input.equals("x")) {
            showMenue();
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    addCourses();
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    ;
                    break;
                case "4":
                    updateCourseDetails();
                    ;
                    break;
                case "5":
                    deleteCourse();
                    ;
                    break;
                case "6":
                    courseSearch();
                    ;
                    break;
                case "7":
                    runninCourse();
                    ;
                    break;
                case "8":
                   addStudent();
                    ;
                    break;
                case "9":
                    showAllStudent();
                    ;
                    break;
                case "10":
                    showStudentById();
                    ;
                    break;

                case "11":
                    studentByName();
                    ;
                    break;
                case "12":
                    studentByBirthYear();
                    ;
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen");
                    break;
                default:
                    inputError();
                    break;
            }

        }
        scanner.close();
    }

    private void studentByBirthYear() {
        {
            System.out.println("Geben Sie ein Geburts-Jahr an!");
            String searchString = scanner.nextLine();
            List<Student> studentList;
            try{
                studentList = stRepo.findStudentByBirthDate(searchString);
                for(Student student : studentList){
                    System.out.println(student);
                }
            } catch(DatabaseException databaseException){
                System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
            }catch(Exception exception){
                System.out.println("Unbekannter Fehler bei der Kurssuche " + exception.getMessage());
            }
        }

    }


    private void studentByName() {
        {
            System.out.println("Geben Sie einen Namen an!");
            String searchString = scanner.nextLine();
            List<Student> studentList;
            try{
                studentList = stRepo.findStudentByName(searchString);
                for(Student student : studentList){
                    System.out.println(student);
                }
            } catch(DatabaseException databaseException){
                System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
            }catch(Exception exception){
                System.out.println("Unbekannter Fehler bei der Kurssuche " + exception.getMessage());
            }
        }

    }

    private void showStudentById() {
        System.out.println("Welcher Student?");
        Long studentId = Long.parseLong(scanner.nextLine());
        try {
            Optional<Student> courseOptional = stRepo.getById(studentId);
            if (courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + studentId + " nicht gefunden!");
            }

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Student-Detailanzeige: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Student-Detailanzeige: " + exception.getMessage());
        }
    }


    private void showAllStudent() {
        List<Student> list = null;


        try {
            list = stRepo.getAll();
            if (list.size() > 0) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Studentenliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Studenten: " + exception.getMessage());
        }
    }

    private void addStudent() {

        String vorname, nachname;
        Date birthDate;


        try {
            System.out.println("Bitte alle Studentendaten angeben:");
            System.out.println("Vorname: ");
            vorname = scanner.nextLine();
            if (vorname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Nachname: ");
            nachname = scanner.nextLine();
            if (nachname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Geburtsdatum (YYYY-MM-DD): ");
            birthDate = Date.valueOf(scanner.nextLine());


            Optional<Student> optionalStudent = stRepo.insert(
                    new Student(vorname, nachname, birthDate)
            );
            if(optionalStudent.isPresent()){
                System.out.println("Kurs angelegt: " + optionalStudent.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());

        } catch (InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());

        } catch (Exception exception) {
            System.out.println("Unbekannter Feler beim Einfügen: " + exception.getMessage());
        }


    }

    private void runninCourse() {
        System.out.println("Aktuell laufende Kurse: ");
        List<Course> list;
        try{
            list = repo.findAllRunningCourses();
            for(Course course : list){
                System.out.println(course);
            }
        } catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: " + databaseException);
        } catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Kurs-Anzeige für laufende Kurse " + exception.getMessage());
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an!");
        String searchString = scanner.nextLine();
        List<Course> courseList;
        try{
            courseList = repo.findAllCourseByNameOrDescription(searchString);
            for(Course course : courseList){
                System.out.println(course);
            }
        } catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler bei der Kurssuche " + exception.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben:");
        Long courseIdToDelete = Long.parseLong(scanner.nextLine());

        try {
            repo.deleteById(courseIdToDelete);

        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Löschen " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fehler beim Löschen " + e.getMessage());
        }
    }

    private void updateCourseDetails() {
        System.out.println("Für welchen Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scanner.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isEmpty()){
                System.out.println("Kurs mit der gegebenen ID nicht in der Datenbank");
            }else{
                Course course = courseOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine änderung gewünscht ist!");
                System.out.println("Name: ");
                name = scanner.nextLine();
                System.out.println("Beschreibung: ");
                description = scanner.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scanner.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                dateFrom = scanner.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scanner.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/DE): ");
                courseType = scanner.nextLine();


                // Bei leerer Eingabe sollen die vorhandenen Daten verwendet werden
                Optional<Course> optionalCourseUpdate = repo.update(
                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate(): Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate(): Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType(): CourseType.valueOf(courseType)

                        )
                );

                optionalCourseUpdate.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        ()-> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );


            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());

        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());

        } catch (Exception exception) {
            System.out.println("Unbekannter Feler beim Einfügen: " + exception.getMessage());
        }



    }

    private void addCourses() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Name: ");
            name = scanner.nextLine();
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Beschreibung");
            description = scanner.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl");
            hours = Integer.parseInt(scanner.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scanner.nextLine());
            System.out.println("Enddatum(YYYY-MM-DD): ");
            dateTo = Date.valueOf(scanner.nextLine());
            System.out.println("Kurstyp: (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scanner.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, dateFrom, dateTo, courseType)
            );
            if(optionalCourse.isPresent()){
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());

        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());

        } catch (Exception exception) {
            System.out.println("Unbekannter Feler beim Einfügen: " + exception.getMessage());
        }


    }

    private void showCourseDetails() {
        System.out.println("Welcher Kurs?");
        Long courseId = Long.parseLong(scanner.nextLine());
        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Detailanzeige: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + exception.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;


        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse: " + exception.getMessage());
        }
    }

    private void showMenue() {
        System.out.println("--------------- Kursmanagement -----------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t " + "(3) Kursdetails anzeigen");
        System.out.println("(4) Kursdetails ändern \t (5) Kurs löschen \t " + "(6) Kurssuche");
        System.out.println("(7) Laufende Kurse \t (8) Student eingeben \t " + "(9) Alle Studenten anzeigen");
        System.out.println("(10) Student anzeigen \t (11) Student nach Namen suchen \t " + "(12) Suche nach Geburtsjahr");

        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
