package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.members.Student;

import java.util.HashMap;

/**
 * Custom double-hashmap specifically for managing spots on a trip
 */
public class SpotMap extends HashMap<Grade, HashMap<Gender, Integer>> {
    private int maxSpots;

    public SpotMap(int maxSpots) {
        this.maxSpots = maxSpots;
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

    public int getMaxSpots(){
        return maxSpots;
    }

    /**
     * Takes a spot away
     * @param student
     */
    public void takeSpot(Student student){
        maxSpots--;

        //set the previous integer to the same number - 1
        //yeah, this is pretty gross
        int prev = get(student.profile.grade, student.profile.gender);
        put(student.profile.grade, student.profile.gender, prev - 1);
    }
}
