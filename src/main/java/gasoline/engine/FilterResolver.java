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
package gasoline.engine;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gasoline.engine.routing.Route;
import gasoline.request.Filter;
import gasoline.request.FilterHandler;

public class FilterResolver {

  private final Map<Route, List<FilterHandler>> filtersByRoute = new HashMap<>();

  public FilterResolver(List<Filter> filters) {
    filters.forEach((filter) -> {
      filter.routes.forEach((route) -> {
        List<FilterHandler> handlers = this.filtersByRoute.getOrDefault(route, new LinkedList<>());
        handlers.add(filter.handler);
        this.filtersByRoute.put(route, handlers);
      });
    });
  }

  public List<FilterHandler> findFiltersFor(Route route) {
    return this.filtersByRoute.getOrDefault(route, emptyList());
  }

}
