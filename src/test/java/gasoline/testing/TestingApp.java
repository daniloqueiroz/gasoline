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
package gasoline.testing;

import static gasoline.Context.ok;
import static java.lang.String.format;

import java.util.Optional;

import gasoline.Application;
import gasoline.Context;
import gasoline.http.StatusCode;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class TestingApp {

  public static class Message {
    public String content;

    public Message(String content) {
      this.content = content;
    }
  }

  public void init(Application app) {
    app.get("/empty", (req) -> {
      return Optional.empty();
    });

    app.get("/present", (req) -> {
      return Optional.of(new Message("Hello, world!"));
    });

    app.get("/message", (req) -> {
      return new Message("Hello, world!");
    });

    app.get("/message_response", (req) -> {
      return ok(new Message("Hello, world!"));
    });

    app.get("/error", (req) -> {
      throw new RuntimeException();
    });

    app.get("/dynamic/{id}/other/{number}", (req) -> {
      return new Message(format("%s:%s", req.attribute("id"), req.attribute("number")));
    });

    app.before((req) -> {
      Optional<String> auth = req.header("auth");
      if (!auth.isPresent() || auth.get() != "secret") {
        Context.abort(StatusCode.UNAUTHORIZED);
      }
    } , app.get("/filtered/{name}", (req) -> {
      return new Message(format("%s:%s", req.attribute("id"), req.attribute("number")));
    }));
  }
}
