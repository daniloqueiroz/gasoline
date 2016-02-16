package gasoline;

import com.google.gson.Gson;

import gasoline.engine.RequestAbortedException;
import gasoline.http.StatusCode;
import gasoline.request.Request;
import gasoline.response.RawResponse;
import gasoline.response.Response;

/**
 * This class contains several static methods to be used when processing a
 * request.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Context {
  public static Gson JSON_TRANSCODER = new Gson();

  /**
   * Transforms the body of the given request from Json to an instance of the
   * given Class.
   * 
   * It raises a {@link RequestAbortedException} with
   * {@link StatusCode#BAD_REQUEST} if the body isn't present.
   */
  public static <T> T fromBody(Request request, Class<T> asClass) {
    if (request.body().isPresent()) {
      String content = request.body().get();
      if (!content.isEmpty()) {
        return JSON_TRANSCODER.fromJson(content, asClass);
      }
    }
    throw new RequestAbortedException(StatusCode.BAD_REQUEST);
  }

  /**
   * Transforms the given json as String to a instace of the given Class.
   */
  public static <T> T fromJson(String json, Class<T> asClass) {
    return JSON_TRANSCODER.fromJson(json, asClass);
  }

  /**
   * Transform the given Object to a json String.
   */
  public static String toJson(Object source) {
    return JSON_TRANSCODER.toJson(source);
  }

  /**
   * Aborts the current request execution and return the given
   * {@link StatusCode}.
   * 
   * <pre>
   * (req) -> {
   *   return Context.abort(StatusCode.SERVER_ERROR);
   * }
   * </pre>
   */
  public static Void abort(StatusCode code) {
    // it returns Void to allow usage inside RequestHandlers
    throw new RequestAbortedException(code);
  }

  public static Response response(StatusCode code) {
    return new RawResponse(code);
  }

  public static Response response(StatusCode code, Object content) {
    return new RawResponse(code, content);
  }

  public static Response ok(Object content) {
    return response(StatusCode.OK, content);
  }

  public static Response created(Object content) {
    return response(StatusCode.CREATED, content);
  }

  public static Response notFound() {
    return response(StatusCode.NOT_FOUND);
  }

  public static Response badRequest() {
    return response(StatusCode.BAD_REQUEST);
  }

  public static Response forbideen() {
    return response(StatusCode.FORBIDEEN);
  }

  public static Response unauthorized() {
    return response(StatusCode.UNAUTHORIZED);
  }
}
