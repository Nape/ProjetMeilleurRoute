package logistic;

import java.time.LocalDate;

/**
 * Petit module de logging.
 */
public class Logger
{
    private static Logger instance = new Logger();

    private Logger() {}

    // TODO: Sauvegarder sur un fichier au lieu de tout domper sur le sysout
    public static void Log(Logger.Severity severity, Class origin, String message)
    {
        StringBuilder builder = new StringBuilder("[" + LocalDate.now() + " - " + origin + "]");
        builder.append(message);

        if (severity == Severity.ERROR)
            System.err.println(builder.toString());
        else
            System.out.println(message);
    }

    public enum Severity
    {
        ERROR,
        INFORMAL
    }
}
