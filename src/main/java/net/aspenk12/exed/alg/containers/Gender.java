package net.aspenk12.exed.alg.containers;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    public final String name;

    Gender(String name) {
        this.name = name;
    }

    /**
     * @throws Exception Translate this exception to a BadDataException at the data read level
     */
    public static Gender getFromString(String s) throws Exception {
        for (Gender gender : values()) {
            if (gender.name.equals(s)) {
                return gender;
            }
        }
        throw new Exception("Couldn't find a Gender associated with String \"" + s + "\"");
    }
}
