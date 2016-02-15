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

    app.before(
        (req) -> {
          Optional<String> auth = req.header("auth");
          if (!auth.isPresent() || auth.get() != "secret") {
            Context.abort(StatusCode.UNAUTHORIZED);
          }
        },
        app.get("/filtered/{name}", (req) -> {
          return new Message(format("%s:%s", req.attribute("id"), req.attribute("number")));
        })
    );
  }
}
