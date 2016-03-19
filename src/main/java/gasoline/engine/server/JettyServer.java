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
package gasoline.engine.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gasoline.engine.GasolineEngine;

public class JettyServer {

  private static final Logger LOG = LoggerFactory.getLogger(JettyServer.class);
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
    LOG.info("Starting Jetty Server on port {}", this.port);
    Server server = this.createServer();
    try {
      GzipHandler gzip = new GzipHandler();
      gzip.setHandler(this.handler);
      server.setHandler(gzip);
      server.dump();
      server.start();
      server.join();
    } catch (Exception e) {
      LOG.error("Unable to start Jetty Server on port {}", this.port);
      LOG.error("Error starting Setty Server", e);
    }
  }

  private Server createServer() {
    return new Server(this.port);
  }
}
