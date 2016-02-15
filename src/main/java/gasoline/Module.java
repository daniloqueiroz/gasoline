package gasoline;

import gasoline.engine.routing.Route;

/**
 * Modules are the cornerstones of an Gasoline {@link Application}.
 *
 * Modules can initialize handlers, define {@link Route}, initialize filter,
 * initialize database connections and so on.
 *
 * As module are functional interfaces, any class that provides a
 * {@link Module#init(Application)} method can be considered an module.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
@FunctionalInterface
public interface Module {

  /**
   * Loads a module
   */
  void init(Application app);

}
