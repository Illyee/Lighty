package dev.schmarrn.lighty.api;

public class LightyColors {
    private static final int GREEN = 0x00FF00;
    private static final int RED = 0xFF0000;

    public static int getWarning() {
        return GREEN;
    }

    public static int getWarningARGB() {
        return GREEN | 0xFF000000;
    }

    public static int getDanger() {
        return RED;
    }

    public static int getDangerARGB() {
        return RED  | 0xFF000000;
    }

    private LightyColors() {}
}
