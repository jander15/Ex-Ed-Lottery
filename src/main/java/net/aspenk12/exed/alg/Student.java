package net.aspenk12.exed.alg;

import net.aspenk12.exed.util.BadEmailException;

public class Student {
    private final int id;
    private final String firstName, lastName;
    private final String email;
    private final Grade grade;
    private final Gender gender;

    /**
     * The To-Be grade of a student when they attend a course
     */
    public enum Grade {
        FRESHMAN(9),
        SOPHOMORE(10),
        JUNIOR(11),
        SENIOR(12);

        public final int gradeNum;

        Grade(int gradeNum) {
            this.gradeNum = gradeNum;
        }
    }

    public enum Gender{
        MALE,
        FEMALE;
    }

    public Student(String firstName, String lastName, String email, Grade grade, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade = grade;
        this.gender = gender;
        id = findID(email);
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
