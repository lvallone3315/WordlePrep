package org.fdu;

/**
 * Defines the ANSI escape codes for outputting colors to the console <br>
 * <p>
 * Color names used by web interface to map game results to CSS colors.
 * <p>
 * <b>Caution:</b> Always append {@link #RESET} after a colored output
 * to return the terminal to its default state.
 */
public class ConsoleColors {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[1;30m";
    public static final String RED = "\u001B[1;31m";
    public static final String GREEN = "\u001B[1;32m";
    public static final String YELLOW = "\u001B[1;33m";
    public static final String GRAY = "\u001B[1;90m";

    private ConsoleColors() {} // utility class, never instantiated
}
