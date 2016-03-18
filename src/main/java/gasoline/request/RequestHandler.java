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
package gasoline.request;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import gasoline.Context;
import gasoline.response.Response;

/**
 * A functional interface that handles a given request.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
@FunctionalInterface
public interface RequestHandler {

  /**
   * Handles a request. This method can return anything, from a {@link Response}
   * object (see builder methods on {@link Context} class), to an Object, or a
   * {@link Future} or {@link CompletableFuture}.
   */
  public Object handle(Request req);
}
