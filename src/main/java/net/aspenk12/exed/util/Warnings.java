package net.aspenk12.exed.util;

import java.util.ArrayList;

/**
 * Keeps track of any strange/notable behavior in the data. Stuff that doesn't warrant an error, but should be investigated.
 *
 * This is probably a bit superfluous, but given that there's a lot of opportunities for things to subtly fail in this program,
 * and because the end user is gonna pretty nontechnical I decided to write it anyways.
 */
public class Warnings {
    private static ArrayList<String> warnings = new ArrayList<>();

    public static void logWarning(String warning){
        warnings.add(warning);
    }

    //todo integrate warning count in the UI
    //todo save warnings
    public static int count(){
        return warnings.size();
    }
}
