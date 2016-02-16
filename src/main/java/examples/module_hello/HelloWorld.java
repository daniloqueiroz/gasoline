package examples.module_hello;

import static gasoline.Context.fromBody;
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

  public static void init(Application app) {
    app.get("/", (req) -> {
      return new HelloWorld();
    });

    app.put("/", (req) -> {
      HelloWorld received = fromBody(req, HelloWorld.class);
      return received;
    });

    app.get("/{name}", (req) -> {
      return new HelloWorld(req.attribute("name"));
    });
  }

  public static void main(String[] args) {
    Application app = new Application(HelloWorld::init); // a module can be just a function
    app.server().onPort(8080).start();
  }
}
