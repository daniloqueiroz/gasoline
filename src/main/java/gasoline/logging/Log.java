package gasoline.logging;

import static java.lang.String.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

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
    return Logger.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
  }

  public static void debug(String message, Object... objects) {
    getLogger().fine(() -> format(message, objects));
  }

  public static void info(String message, Object... objects) {
    getLogger().info(() -> format(message, objects));
  }

  public static void error(String message, Object... objects) {
    getLogger().severe(() -> format(message, objects));
  }

  public static void error(Throwable t, String message, Object... objects) {
    getLogger().severe(() -> {
      String msg = format(message, objects);
      StringWriter stackTrace = new StringWriter();
      t.printStackTrace(new PrintWriter(stackTrace));
      msg = format("%s\n%s", msg, stackTrace.toString());
      return msg;
    });
  }
}
