package edu.cmu.cs.cs214.rec06.loggingsystem;

/**
 * The entry class to the logging application.
 */
public class Main {

	/**
	 * Creates a logger. Writes some debug and error messages.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Logger logger = new Logger();
		LoggingListener consoleListener = new ConsoleListener();
		LoggingListener logFileListener = new LogfileListener();

		logger.addLoggingListener(consoleListener);
		logger.addLoggingListener(logFileListener);

		logger.logDebug("This is a debug message.");
		logger.logError("This is an error message.");

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 5; j++) {
				// Create a generic case when debug messages are printed
				logger.logDebug(String.format("i=%s, j=%s", i, j));

				// Create a specific case when an "error" is detected
				if (i == 1 && j == 3) {
					logger.logError(String.format(
							"Illegal state detected! i=%s, j=%s", i, j));
				}
			}
		}

		// Shut down the logger
		logger.logClose();
	}
}
