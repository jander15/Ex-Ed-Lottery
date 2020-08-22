package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Pick;

import java.util.List;
import java.util.Map;

public class Algorithm {
    private Map<String, Course> courses;
    private List<Student> students;

    public Algorithm(Map<String, Course> courses, List<Student> students) {
        this.courses = courses;
        this.students = students;
    }

    public void run(){
        for (Student student : students) {
            applyToNext(student);
        }
    }

    /**
     * Attempts to fit a student on to it's next choice of course.
     * This method acts recursively. If the student fails to get on a course,
     * or if the student outbids another student, this method calls applyToNext on one of those students accordingly
     */
    /*protected 4 test*/ static void applyToNext(Student student){
        List<Pick> picks = student.application.getPicks();

        Course course = student.currentPick.course;

        Student nextStudent = course.placeStudent(student);

        //if the next student is null, no students were outbid and our job is done here.
        if(nextStudent == null){
            return;
        }

        //if any student ever has no more trips to try, add them to unlucky and finish
        if(nextStudent.advancePick()){
            //todo, add unlucky students array
            return;
        };

        //seccy recursion
        applyToNext(nextStudent);
    }
}
