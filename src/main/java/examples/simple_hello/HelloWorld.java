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
package examples.simple_hello;

import static java.lang.String.format;

import gasoline.Application;

public class HelloWorld {

  public final String message;

  public HelloWorld() {
    this("Strange");
  }

  public HelloWorld(String name) {
    this.message = format("Hello, %s!", name);
  }

  public static void main(String[] args) {
    Application app = new Application();

    app.get("/", (req) -> {
      return new HelloWorld();
    });

    app.get("/{name}", (req) -> {
      return new HelloWorld(req.attribute("name"));
    });

    app.server().onPort(8080).start();
  }
}
