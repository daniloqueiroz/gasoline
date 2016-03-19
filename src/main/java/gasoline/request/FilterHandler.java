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

import gasoline.Context;

@FunctionalInterface
public interface FilterHandler {
  /**
   * Filters a request. This method can modify the request (adding attributes)
   * and should return nothing. However, it can abort the current request using
   * {@link Context#abort(gasoline.http.StatusCode)} method.
   */
  public void filter(Request req);

}
