package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;

/**
 * A student's pick, containing a course and a bid value.
 */
public class Pick {
    public final Course course;
    public int bid; //points can be modified in validation

    public Pick(Course course, int bid) {
        this.course = course;
        this.bid = bid;
    }
}
