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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import gasoline.engine.GasolineEngine;
import gasoline.http.HttpMethod;
import gasoline.response.Response;
import gasoline.utils.Log;

class JettyHandler extends AbstractHandler {

  private final GasolineEngine engine;

  public JettyHandler(GasolineEngine engine) {
    this.engine = engine;
  }

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) throws IOException, ServletException {

    gasoline.request.Request request = this.createRequest(servletRequest);
    Response response = this.engine.process(request);
    this.prepareResponse(servletResponse, response);
    baseRequest.setHandled(true);
  }

  private gasoline.request.Request createRequest(HttpServletRequest servletRequest) throws IOException {
    gasoline.request.Request req;
    // Creates the request with PATH, METHOD and BODY (if there's a body)
    if (servletRequest.getContentType() != null
        && servletRequest.getContentType().startsWith(MimeTypes.Type.APPLICATION_JSON.asString())) {
      try (Scanner sc = new Scanner(servletRequest.getReader())) {
        StringBuilder buf = new StringBuilder();
        while (sc.hasNextLine()) {
          buf.append(sc.nextLine());
        }
        req = new gasoline.request.Request(servletRequest.getPathInfo(), HttpMethod.valueOf(servletRequest.getMethod()),
            buf.toString());
      }
    } else {
      req = new gasoline.request.Request(servletRequest.getPathInfo(), HttpMethod.valueOf(servletRequest.getMethod()));
    }

    // Set HEADERS
    for (Enumeration<String> e = servletRequest.getHeaderNames(); e.hasMoreElements();) {
      String name = e.nextElement();
      req.header(name, servletRequest.getHeader(name));
    }

    // Set Attributes (ignore non-String attributes)
    for (Enumeration<String> e = servletRequest.getAttributeNames(); e.hasMoreElements();) {
      String name = e.nextElement();
      Object attribute = servletRequest.getAttribute(name);
      if (attribute instanceof String) {
        req.attribute(name, (String) attribute);
      }
    }

    // Set Query Parameters
    for (Enumeration<String> e = servletRequest.getParameterNames(); e.hasMoreElements();) {
      String name = e.nextElement();
      req.parameter(name, servletRequest.getParameter(name));
    }

    return req;
  }

  private void prepareResponse(HttpServletResponse servletResponse, Response response) {
    servletResponse.setStatus(response.statusCode().code);
    servletResponse.setContentType(MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
    response.body().ifPresent((body) -> {
      try {
        servletResponse.getWriter().write(body);
      } catch (Exception e) {
        Log.error(e, "Error writing response: {}", body);
      }
    });
    // TODO set headers
  }

}
