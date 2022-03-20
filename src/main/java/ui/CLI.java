package ui;

import dataacces.DatabaseException;
import dataacces.MyCourseRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLI {

    Scanner scanner;
    MyCourseRepository repo;

    public CLI(MyCourseRepository repo) {
        this.scanner = new Scanner(System.in);
        this.repo = repo;
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
        System.out.println("(7) Laufende Kurse \t (-) xxxx \t " + "(-) xxxx");

        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
