package net.aspenk12.exed.util;

/**
 * Exception thrown upon parsing data that fails to match the internal workings of the algorithm for any reason.
 */
public class BadDataException extends RuntimeException{

    /**
     * @param csv The CSV that the bad data can be found in
     * @param row The row of the bad data
     * @param col The column of the bad data
     * @param data The string version of the bad data
     */
    public BadDataException(CSV csv, int row, int col, String data) {
        super(makeString(csv, row, col, data));
    }

    /**
     * Terse constructor
     */
    public BadDataException(CSV csv, String data){
        super(makeString(csv, null, null, data));
    }

    /**
     * Standard form constructor
     */
    public BadDataException(String message){
        super(message);
    }

    private static String makeString(CSV csv, Integer row, Integer col, String data){
        String FilePath = csv.getFile().getPath();

        String rowString = " Row: " + row;
        String colString = " Column: " + col;

        return "A CSV contained data incompatible with the algorithm. File: " + FilePath + rowString + colString + " Bad Data: " + data;
    }
}
