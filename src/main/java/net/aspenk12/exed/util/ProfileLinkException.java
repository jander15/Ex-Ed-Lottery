package net.aspenk12.exed.util;

/**
 * Thrown if a student (as created with the course application data) is unable to be linked to a validated profile.
 * @see net.aspenk12.exed.alg.members.Student
 * @see net.aspenk12.exed.alg.members.Profile
 */
public class ProfileLinkException extends Exception{
    public final String email;
    public final int id;

    public ProfileLinkException(String email, int id) {
        super("No data exists for a student. Email: " + email + "; id: " + id);

        this.email = email;
        this.id = id;
    }
}
