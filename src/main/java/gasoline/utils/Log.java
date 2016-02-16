package gasoline.utils;

import static java.lang.String.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A very simple static class to log using SLF4J. It discovers the caller class
 * name and use it as the logger name.
 *
 * @see Logger
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Log {
  private static Logger getLogger() {
    // magic number: 3 - 0 Thread..; 1 and 2 gasoline.Log; 3 caller class
    return LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
  }

  public static void debug(String message, Object... objects) {
    getLogger().debug(message, objects);
  }

  public static void info(String message, Object... objects) {
    getLogger().info(message, objects);
  }

  public static void error(String message, Object... objects) {
    getLogger().error(message, objects);
  }

  public static void error(Throwable t, String message, Object... objects) {
    StringWriter stackTrace = new StringWriter();
    t.printStackTrace(new PrintWriter(stackTrace));
    message = format("%s\n%s", message, stackTrace.toString());
    getLogger().error(message, objects);
  }
}
