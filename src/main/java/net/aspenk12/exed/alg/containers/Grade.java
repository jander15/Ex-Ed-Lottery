package net.aspenk12.exed.alg.containers;

/**
 * The To-Be grade of a student when they attend a course
 */
public enum Grade {
    FRESHMAN(9, "Freshman"),
    SOPHOMORE(10, "Sophomore"),
    JUNIOR(11, "Junior"),
    SENIOR(12, "Senior");

    public final int gradeNum;
    public final String name;


    Grade(int gradeNum, String name) {
        this.gradeNum = gradeNum;
        this.name = name;
    }

    /**
     * @throws Exception Translate this exception to a BadDataException at the data read level
     */
    public static Grade getFromInt(int i) throws RuntimeException {
        for (Grade grade : values()) {
            if (grade.gradeNum == i) {
                return grade;
            }
        }
        throw new RuntimeException("Couldn't find a Grade associated with interger \"" + i + "\"");
    }
}
