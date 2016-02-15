package gasoline.engine;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public interface Engine<T, V> {

  /**
   * This is the entry point for any engine. Engines usually adapts the http
   * request type from a specific server implementation to a gasoline request,
   * and do the other way around for responses - adapting from gasoline response
   * to a server http response.
   *
   * Every supported server has to implement its engine's handle method.
   */
  void handle(T httpRequest, V httpResponse);

}
