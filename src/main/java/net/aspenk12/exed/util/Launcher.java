package net.aspenk12.exed.util;

/**
 * The net.aspenk12.exed.util.Launcher Class is a stupid workaround that acts as a proxy for the actual net.aspenk12.exed.util.Main class.
 * It prevents us from having to specify JFX as modules in the launch configs. Should mean minimal setup from machine to machine
 * https://stackoverflow.com/questions/52578072/gradle-openjfx11-error-javafx-runtime-components-are-missing
 * @see Main
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
