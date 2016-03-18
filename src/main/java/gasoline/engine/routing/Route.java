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
package gasoline.engine.routing;

import static gasoline.engine.routing.PathUtils.getAttributesNames;
import static gasoline.engine.routing.PathUtils.normalizePath;
import static java.util.Objects.hash;

import java.util.List;

import gasoline.http.HttpMethod;
import gasoline.request.RequestHandler;

/**
 * A gasoline Route.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Route {

  public final String path;
  public final HttpMethod method;
  public final RequestHandler handler;
  public final List<String> attributes;

  public Route(String path, HttpMethod method, RequestHandler handler) {
    this.path = normalizePath(path);
    this.method = method;
    this.handler = handler;
    this.attributes = getAttributesNames(path);
  }

  @Override
  public String toString() {
    return "Route [path=" + this.path + ", method=" + this.method + "]";
  }

  @Override
  public int hashCode() {
    return hash(this.path, this.method);
  }

  @Override
  public boolean equals(Object obj) {
    boolean equals = false;
    if (obj instanceof Route) {
      Route other = (Route) obj;
      equals = this.path.equals(other.path) && this.method.equals(other.method);
    }
    return equals;
  }
}
