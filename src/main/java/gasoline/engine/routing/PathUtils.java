package gasoline.engine.routing;

import static java.lang.String.format;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class PathUtils {
  public static final Pattern PATH_REGEX = Pattern.compile("^/(([\\w\\.\\-_~]+/)*(\\{[\\w_]+\\}/)*)*$");

  /**
   * Checks whether a given {@link Route} path is a dynamic path.
   */
  public static boolean isDynamicPath(String routePath) {
    return routePath.matches(".*(\\{[\\w\\.\\-_~]+\\}/).*");
  }

  /**
   * Replaces variable names from a {@link Route} path with a regex pattern.
   */
  public static String dynamicPathRegex(String path) {
    return path.replaceAll("(\\{[\\w\\.\\-_~]+\\})", "([\\\\w_]+)");
  }

  /**
   * Normalizes a path:
   *
   * <pre>
   * * ensure it has a trailing slash;
   * * REGEX validation;
   * * put to lower case;
   * </pre>
   */
  public static String normalizePath(String path) {
    String normalized = ensureTrailingSlash(path);
    validatePath(normalized);
    return toLowerCase(normalized);
  }

  private static String ensureTrailingSlash(String path) {
    return path.endsWith("/") ? path : format("%s/", path);
  }

  private static void validatePath(String normalized) {
    if (!PATH_REGEX.matcher(normalized).matches()) {
      throw new IllegalArgumentException("Path contains illegal characteres.");
    }
  }

  private static String toLowerCase(String normalized) {
    StringBuilder buf = new StringBuilder();
    int from = 0;
    do {
      int index = normalized.indexOf('{', from);
      if (index != -1) {
        buf.append(normalized.substring(from, index).toLowerCase());
        from = normalized.indexOf('}', from) + 1;
        buf.append(normalized.substring(index, from));
      } else {
        buf.append(normalized.substring(from).toLowerCase());
        from = -1;
      }
    } while (from != -1);
    return buf.toString();
  }

  public static List<String> getAttributesNames(String path) {
    LinkedList<String> attributes = new LinkedList<>();
    int from = 0;
    do {
      int index = path.indexOf('{', from);
      if (index != -1) {
        from = path.indexOf('}', index);
        attributes.add(path.substring(index + 1, from));
      } else {
        from = index;
      }
    } while (from != -1);
    return attributes;
  }
}
