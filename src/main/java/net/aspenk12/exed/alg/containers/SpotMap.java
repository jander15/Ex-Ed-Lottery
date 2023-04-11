package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Student;

import java.util.HashMap;

/**
 * Custom double-hashmap specifically for managing spots on a trip
 */
public class SpotMap extends HashMap<Grade, HashMap<Gender, Integer>> {
    private int remainingSpots;
    public final int maxSpots;

    public SpotMap(int maxSpots) {
        this.maxSpots = maxSpots;
        remainingSpots = maxSpots;
    }

    public void put(Grade grade, Gender gender, int spots) {
        HashMap<Gender, Integer> genderMap = get(grade);

        if (genderMap == null) {
            genderMap = new HashMap<>();
            put(grade, genderMap);
        }
        //if gendermap exists, it's already in the encompassing map
        genderMap.put(gender, spots);
    }

    public int get(Grade grade, Gender gender) {
        return get(grade).get(gender);
    }

    public int getRemainingSpots(){
        return remainingSpots;
    }

    /**
     * Takes a spot away
     * @param student
     */
    public void takeSpot(Student student){
        remainingSpots--;

        //set the previous integer to the same number - 1
        //yeah, this is pretty gross
        int prev = get(student.profile.getGrade(), student.profile.getGender());

        put(student.profile.getGrade(), student.profile.getGender(), prev - 1);

        if (!student.profile.getGender().equals(Gender.X)){
            prev=get(student.profile.getGrade(), Gender.X);
            put(student.profile.getGrade(), Gender.X, prev - 1);
        }

    }

    public void addSpot(Student student){
        remainingSpots++;

        //set the previous integer to the same number + 1
        //one could factor out the shared code between this and takeSpot() if they pleased.
        int prev = get(student.profile.getGrade(), student.profile.getGender());

        put(student.profile.getGrade(), student.profile.getGender(), prev + 1);

        if (!student.profile.getGender().equals(Gender.X)){
            prev=get(student.profile.getGrade(), Gender.X);
            put(student.profile.getGrade(), Gender.X, prev + 1);
        }
    }
}
