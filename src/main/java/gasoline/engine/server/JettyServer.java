package gasoline.engine.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

import gasoline.engine.GasolineEngine;
import gasoline.utils.Log;

public class JettyServer {

  public static final int DEFAULT_PORT = 8080;
  /*
   * TODO list:
   * jetty server configuration methods
   * enable http/2
   * reasonable defaults (threads and io)
   */
  private final JettyHandler handler;
  private int port = DEFAULT_PORT;

  public JettyServer(GasolineEngine engine) {
    this.handler = new JettyHandler(engine);
  }

  public JettyServer onPort(int port) {
    this.port = port;
    return this;
  }

  public void start() {
    Log.info("Starting Jetty Server on port {}", this.port);
    Server server = this.createServer();
    try {
      GzipHandler gzip = new GzipHandler();
      gzip.setHandler(this.handler);
      server.setHandler(gzip);
      server.dump();
      server.start();
      server.join();
    } catch (Exception e) {
      Log.error(e, "Unable to start Jetty Server on port {}", this.port);
    }
  }

  private Server createServer() {
    return new Server(this.port);
  }
}
