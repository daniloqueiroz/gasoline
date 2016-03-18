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
