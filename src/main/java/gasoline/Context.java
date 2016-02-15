package gasoline;

import com.google.gson.Gson;

import gasoline.engine.RequestAbortedException;
import gasoline.http.StatusCode;
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

  public static <T> T fromJson(String json, Class<T> asClass) {
    return JSON_TRANSCODER.fromJson(json, asClass);
  }

  public static String toJson(Object source) {
    return JSON_TRANSCODER.toJson(source);
  }

  public static Void abort(StatusCode code) {
    // it returns Void to allow usage inside RequestHandlers
    // (req) -> {return Context.abort(StatusCode.SERVER_ERROR);}
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
}
