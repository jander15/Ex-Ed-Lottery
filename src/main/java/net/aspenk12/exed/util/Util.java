package net.aspenk12.exed.util;

import net.aspenk12.exed.alg.members.Student;

/**
 * Contains misc. static utility methods. Good place to put stuff that needs to be tested.
 */
public class Util {

    /**
     * Echos Integer.parseInt(), but automatically wraps with a try/catch and translates to BadDataException
     */
    public static int parseIntCSV(String string, CSV csv, int row, int col){
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e){
            throw new BadDataException(csv, row, col, string);
        }
    }

    /**
     * Finds the ID in a student email and returns it as an integer.
     */

    /**
     * This might break when adding application data.
     * if it does just try adding application data again.
     */
    public static int getIDFromEmail(String email){
        int i = 0;
        int atIndex = email.indexOf("@"); //finds the index of the at symbol in the student's email
        if (atIndex==-1){
            System.out.println("Try adding application data again.");
        }


        String idString = email.substring(atIndex - 5, atIndex);

        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e){
            throw new BadDataException("A bad email was in your student dataset, the algorithm was unable to deduce the student id from this Email: " + email);
        }
    }

    /**
     * Takes string data from a course application and extracts the course ID from it.
     * For example:
     * "Joey's Homeland <JH>" -> "JH"
     */
    public static String extractCourseID(String input){
        int caretIndex = input.indexOf('<');

        if(caretIndex == -1){
            throw new BadDataException("Failed to extract a course ID from String \"" + input + "\"");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(caretIndex + 1));
        sb.append(input.charAt(caretIndex + 2));

        return sb.toString();
    }

    private Util() {}
}
