package net.aspenk12.exed.alg.data;
/**
 * Contains the weights for Adjusted Course Demand Index (ACDI) calculations
 *
 * The ACDI is intended to be a metric for course demand nondependent on course expenditure.
 * Thus, small courses and large courses can be compared, regardless of the effect of scarcity on point expenditure.
 * @see CourseData
 *
 * https://docs.google.com/spreadsheets/d/1RWVjrQlFMbxiu4orwcunpkta7loNvO6UbedTHAlONU8/edit?usp=sharing
 *
 * @author Alex Appleby
 */
public class ACDI {

    /**
     * Gets a proper weight for a particular course index.
     * @param i, the index of the course in a student's list of bids (from 0 - 11) corresponding to 1st - 12th picks.
     * @return the ACDI value on a weighed scale.
     * @throws Error if attempt is made to weigh an invalid ACDI pick
     *
     * Graph / Spreadsheet of weights:
     * https://docs.google.com/spreadsheets/d/1RWVjrQlFMbxiu4orwcunpkta7loNvO6UbedTHAlONU8/edit?usp=sharing
     */
    public static int weigh(int i){
        return switch (i) {
            case 0 -> 6;
            case 1 -> 4;
            case 2 -> 3;
            case 3 -> 2;
            case 4, 5, 6, 7, 8, 9, 10, 11 -> 1;
            default -> throw new Error("Tried to get an ACDI weight out of the proper range");
        };
    }
}
