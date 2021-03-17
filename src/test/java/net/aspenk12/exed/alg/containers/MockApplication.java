package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;

public class MockApplication extends Application{

    /**
     * Applications must have one pick
     */
    public MockApplication(Course course, int bid) {
        super(null, null);
        addNewPick(course, bid);
    }
}
