package gasoline.response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class ResponseFuture implements Future<Response> {

  private Response result;
  private final CountDownLatch latch = new CountDownLatch(1);

  public void set(Response result) {
    this.latch.countDown();
    this.result = result;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return this.result != null;
  }

  @Override
  public Response get() throws InterruptedException, ExecutionException {
    this.latch.await();
    return this.result;
  }

  @Override
  public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    this.latch.await(timeout, unit);
    return this.result;
  }
}
