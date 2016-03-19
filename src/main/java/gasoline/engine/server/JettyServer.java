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

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gasoline.engine.GasolineEngine;

public class JettyServer {

  private static final Logger LOG = LoggerFactory.getLogger(JettyServer.class);
  public static final int DEFAULT_PORT = 8080;
  public static final int DEFAULT_MAX_THREADS = 500;
  public static final int DEFAULT_OUTPUT_BUFFER_SIZE = 32768;
  public static final int DEFAULT_HEADERS_SIZE = 8192;
  private static final int DEFAULT_IDLE_CONNECTION_TIMEOUT = 30000;

  private final GasolineEngine engine;
  private int port = DEFAULT_PORT;
  private int numThreads = DEFAULT_MAX_THREADS;
  private int outBufferSize = DEFAULT_OUTPUT_BUFFER_SIZE;
  private int headersSize = DEFAULT_HEADERS_SIZE;
  private int connectionTimeout = DEFAULT_IDLE_CONNECTION_TIMEOUT;

  public JettyServer(GasolineEngine engine) {
    this.engine = engine;
  }

  public JettyServer onPort(int port) {
    this.port = port;
    return this;
  }

  public JettyServer maxThreads(int numThreads) {
    this.numThreads = numThreads;
    return this;
  }

  public JettyServer outputBufferSize(int size) {
    this.outBufferSize = size;
    return this;
  }

  public JettyServer headersSize(int size) {
    this.headersSize = size;
    return this;
  }

  public JettyServer idleConnectionTimeout(int millis) {
    this.connectionTimeout = millis;
    return this;
  }

  public void start() {
    LOG.info("Starting Jetty Server on port {}", this.port);
    Server server = this.createServer();
    server.setHandler(createHandlers());
    try {
      server.start();
      LOG.info(server.dump());
      server.join();
    } catch (Exception e) {
      LOG.error("Unable to start Jetty Server on port {}", this.port);
      LOG.error("Error starting Setty Server", e);
    }
  }

  private GzipHandler createHandlers() {
    GzipHandler gzip = new GzipHandler();
    gzip.setHandler(new JettyHandler(this.engine));
    return gzip;
  }

  private Server createServer() {
    Server server = new Server(new QueuedThreadPool(this.numThreads));

    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setOutputBufferSize(this.outBufferSize);
    httpConfig.setRequestHeaderSize(this.headersSize);
    httpConfig.setResponseHeaderSize(this.headersSize);
    httpConfig.setSendServerVersion(true);
    httpConfig.setSendDateHeader(false);
    ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
    http.setPort(this.port);
    http.setIdleTimeout(this.connectionTimeout);
    server.addConnector(http);

    return server;
  }
}
