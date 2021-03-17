package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Profile;

import java.util.List;

/**
 * Profile mock with null construction built in and setters to control stuff in lazy mode
 */
public class MockProfile extends Profile {
    public MockProfile() {
        super(-1, null, null, null, null, -1, -1, null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLottoNumber(int lottoNumber) {
        this.lottoNumber = lottoNumber;
    }
}
