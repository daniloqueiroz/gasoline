/**
 * Gasoline  Copyright (C) 2015  daniloqueiroz.github.io/gasoline
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
