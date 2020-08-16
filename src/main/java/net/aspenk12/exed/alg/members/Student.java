package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.util.BadDataException;
import net.aspenk12.exed.util.BadEmailException;
import net.aspenk12.exed.util.CSV;

import java.util.ArrayList;

public class Student {
    private final int id;
    private final String firstName, lastName;
    private final String email;
    private final Grade grade;
    private final Gender gender;

    private static ArrayList<Student> students;

    public static void createStudents(CSV csv){
        //only create students once, duh.
        if(students != null) return;

        students = new ArrayList<>();

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            //todo make reflect actual csv
            String email = row[0];
            String firstName = row[1];
            String lastName = row[2];

            String genderString = row[3];
            Gender gender;

            try {
                gender = Gender.getFromString(genderString);
            } catch (Exception e) {
                throw new BadDataException(csv, i, 3, genderString);
            }

            String gradeString = row[4];
            Grade grade;

            try {
                grade = Grade.getFromString(gradeString);
            } catch (Exception e){
                throw new BadDataException(csv, i, 4, gradeString);
            }

            new Student(firstName, lastName, email, grade, gender);
        }
    }

    /*protected for testing*/ Student(String firstName, String lastName, String email, Grade grade, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade = grade;
        this.gender = gender;
        id = findID(email);

        students.add(this);
    }

    /*protected for testing*/ static int findID(String email){
        int atIndex = email.indexOf("@"); //finds the index of the at symbol in the student's email
        String idString = email.substring(atIndex - 5, atIndex);

        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e){
            throw new BadEmailException("A bad email was in your student dataset, the algorithm was unable to deduce the student id from the Email", email);
        }
    }
}
