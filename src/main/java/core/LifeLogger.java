package core;

import java.util.logging.*;

/**
 * LifeLogger is a utility class for logging actions and events in the application.
 * It provides methods to log messages at different levels (DEBUG, INFO, WARN, ERROR).
 * The logger is configured to format log messages with a specific pattern.
 */
public class LifeLogger {
    private final Logger log;
    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;
    private static final Level DEBUG_LOG_LEVEL = Level.FINER;
    private static final Level DEVELOPMENT_LOG_LEVEL = Level.INFO;
    private static final Level RELEASE_LOG_LEVEL = Level.WARNING;

    /**
     * Constructor for LifeLogger. Uses the default log level.
     * @param clazz The class for which the logger is created. Is used to set the logger name.
     */
    public LifeLogger(Class<?> clazz) {
        Level level = getLogLevel();
        log = Logger.getLogger(clazz.getName());
        if (log.getHandlers().length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    // Extract only the last two segments
                    String[] pathSegments = logRecord.getLoggerName().split("\\.");
                    String relativePath = pathSegments.length >= 1
                            ? pathSegments[pathSegments.length - 1]
                            : logRecord.getLoggerName();

                    String time = new java.text.SimpleDateFormat("HH:mm:ss.SSS")
                            .format(new java.util.Date(logRecord.getMillis()));

                    return String.format("[%s] [%s] %s: %s%n",
                            time,
                            logRecord.getLevel(),
                            relativePath,
                            logRecord.getMessage());
                }
            });
            handler.setLevel(level);
            log.addHandler(handler);
        }
        log.setLevel(level);
        log.setUseParentHandlers(false);
    }

    /**
     * Finds the log Level in the System property "appMode".
     * @return the Level
     */
    private Level getLogLevel() {
        String appMode = System.getProperty("appMode", "development");
        return switch (appMode.toLowerCase()) {
            case "debug" -> DEBUG_LOG_LEVEL;
            case "development" -> DEVELOPMENT_LOG_LEVEL;
            case "release" -> RELEASE_LOG_LEVEL;
            default -> DEFAULT_LOG_LEVEL;
        };
    }

    /**
     * Log detailed debugging information, generally for tracing the application flow.
     * @param message The message to log.
     */
    public void debug(String message) {
        log.fine(message);
    }

    /**
     * Log general informational messages to indicate normal operations.
     * @param message The message to log.
     */
    public void info(String message) {
        log.info(message);
    }

    /**
     * Log potential problems that are non-critical but require attention.
     * @param message The message to log.
     */
    public void warn(String message) {
        log.warning(message);
    }

    /**
     * Log critical issues that indicate a serious failure in the application.
     * @param message The message to log.
     */
    public void error(String message) {
        log.severe(message);
    }

    /**
     * Log critical issues that indicate a serious failure in the application, with an exception.
     * @param message The message to log.
     * @param throwable The exception to log.
     */
    public void error(String message, Throwable throwable) {
        log.log(Level.SEVERE, message, throwable);
    }
}