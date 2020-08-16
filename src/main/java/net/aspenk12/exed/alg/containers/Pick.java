package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;

/**
 * A student's pick, containing a course and a point value.
 */
public class Pick {
    public final Course course;
    public final int points;

    public Pick(Course course, int points) {
        this.course = course;
        this.points = points;
    }
}
