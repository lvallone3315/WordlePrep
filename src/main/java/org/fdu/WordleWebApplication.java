package org.fdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for Springboot web interface (as well as redirect to console version)
 * SpringBootApplication annotation triggers: auto-configuration, component scanning and
 *   marking this class as the configuration source
 */
@SpringBootApplication
public class WordleWebApplication {

    /**
     * Springboot main() - by default starts game web interface,
     * but if --console in arg list, starts console version
     * @param args argument list passed in from the command line (e.g. java -jar [jar file] --console
     */
    public static void main(String[] args) {
        if (ifConsoleFlag(args)) {
            Main.playWordleConsole();
        }
        else {
            SpringApplication.run(WordleWebApplication.class, args);
        }
    }

    /**
     * helper funtion to check argument list for --console option
     * @param args argument list passed into the spring boot on application invocation
     * @return true if --console listed anywhere in argument list, false otherwise
     */
    private static boolean ifConsoleFlag(String[] args) {
        for (String arg: args) {
            if (arg.equals("--console")) {
                return true;
            }
        }
        return false;
    }
}