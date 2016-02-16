package gasoline.utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Pair<T> {
  public final T _1;
  public final T _2;

  public Pair(T _1, T _2) {
    this._1 = _1;
    this._2 = _2;
  }

  public static class Pairs<T> extends LinkedList<Pair<T>> {

    private static final long serialVersionUID = 1103152358985771995L;

    @SafeVarargs
    public Pairs(Pair<T>... pairs) {
      super();
      this.addAll(Arrays.asList(pairs));
    }

    public Map<T, T> asMap() {
      Map<T, T> map = new HashMap<>();
      this.forEach((p) -> {
        map.put(p._1, p._2);
      });
      return map;
    }
  }

  public static <T> Pair<T> p(T _1, T _2) {
    return new Pair<T>(_1, _2);
  }

  @SafeVarargs
  public static <T> Pairs<T> pairs(Pair<T>... pairs) {
    return new Pairs<T>(pairs);
  }
}
