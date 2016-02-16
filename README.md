# Gasoline

[![Build Status](https://travis-ci.org/daniloqueiroz/gasoline.svg?branch=master)](https://travis-ci.org/daniloqueiroz/gasoline)

Gasoline is a very simple Micro Framework for build REST APIs using Java 8.

One of the main goals of Gasoline is be as less opinionated as possible
so it comes with no batteries included - no configuration, nor IoC, nor template support 
and Log support is build using **SLF4J**.

As it was built with REST API in mind, it only support *JSon* data, using [**GSon**](https://github.com/google/gson) 
library.

Other dependencies includes **Jetty**, as *HTTP/2* server. And that's all!

# Hello World

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

See the *examples* package for more examples.

# Application and Modules

The **Application** is the entry point for a Gasoline Application. You can add your
routes direct to the application, or you can use **Modules** to split your application
on several small components.

# Routes

Routes define a **RequestHandler** to a path. The path should be relative and start with a "**/**".

Also, you can use dynamic attributes on your path, defining then using curly-braces. The dynamic
attributes can be retrieved later using the method **Request.attribute(String name)**.

    app.get("/{name}", (req) -> {
      return new HelloWorld(req.attribute("name"));
    });

# Request Handlers

A **RequestHandler** is a function that receives a *Request* as parameter and
return an *Object* as response.

It can return basically anything, a *Response*, generated using **Context** help methods,
or an arbitrary *Object*, that will be rendered as Json using **Context#toJSon** method, or
even a **Future** or a **CompletableFuture** which will be processed asynchronously.

We will talk more about *asynchronous handlers* later on.

# Filters

Filters can be used to modify **Requests** before the execution of the **RequestHandler**, or even
to verify pre-conditions and interrupt the flow.
Filters are pretty much like a **RequestHandler** but they don't return anything.

    app.filter(
      (req) -{
        Optional<String> auth = req.header("auth");
        if (!auth.isPresent() || auth.get() != "secret") {
          Context.abort(StatusCode.UNAUTHORIZED);
        }
      }, 
      app.get("/", (req) -> {
        return new HelloWorld();
      });
    );

# Request

# Context

The context class provides utilities methods to be used in the context of a **RequestHandler**.
Such methods can help you transcode *Objects* to *Json* and vice-versa, create custom *Responses*,
with different *Status Codes*, and much more.

Here goes a non-exhaustive list of methods provided by the **Context** class.

* fromJson(String json, Class<?> asClass)
* toJson(Object obj)
* response(StatusCode sc)
* response(StatusCode sc, Object obj)
* ok - alias for *response(StatusCode.OK)*
* created(Object obj) - alias for *response(StatusCode.CREATED, obj)*
* notfound() - alias for *response(StatusCode.NOT_FOUND)*
* badRequest() - alias for *response(StatusCode.BAD_REQUEST)*

Note that as all the methods are static, you can use static imports.

# Async

Not Supported Yet!

# Server

As said before, it uses **Jetty Server** as embedded web server. For now it isn't supported to
replace Jetty, neither deploy a **Gasoline** application on any other Container Server.

The **JettyServer** instance can be obtained as follows:

    JettyServer server = app.server();
    server.onPort(7777).start();

The **JettyServer** instances provides methods to configure some of it's internals settings,
but we try to set reasonable defaults:

* **Default Port**: 8080

Finally, **GZip** compression support is active.

