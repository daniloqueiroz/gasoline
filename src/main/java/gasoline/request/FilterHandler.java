package gasoline.request;

import gasoline.Context;

@FunctionalInterface
public interface FilterHandler {
    /**
     * Filters a request. This method can modify the request (adding attributes)
     * and should return nothing. However, it can abort the current request using
     * {@link Context#abort(gasoline.http.StatusCode)} method.
     */
    public void filter(Request req);

}
