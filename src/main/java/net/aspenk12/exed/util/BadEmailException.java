package net.aspenk12.exed.util;

public class BadEmailException extends RuntimeException {
    public BadEmailException(String message, String email) {
        super(message + " Email: " + email);
    }

    public BadEmailException(String email){
        this("An unusable email was found in your dataset.", email);
    }
}
