package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;

/**
 * A student's pick, containing a course and a bid value.
 */
public class Pick {
    public final Course course;
    public final int index; //the position of this bid in it's student's application
    public int bid; //points can be modified in validation

    public Pick(Course course, int bid, int index) {
        this.course = course;
        this.bid = bid;
        this.index = index;
    }
}
