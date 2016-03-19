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

import java.util.Arrays;
import java.util.List;

import gasoline.engine.routing.Route;

public class Filter {

  public final List<Route> routes;
  public final FilterHandler handler;

  public Filter(FilterHandler filter, Route... routes) {
    this(filter, Arrays.asList(routes));
  }
  
  public Filter(FilterHandler filter, List<Route> routes) {
    this.handler = filter;
    this.routes = routes;
  }
}