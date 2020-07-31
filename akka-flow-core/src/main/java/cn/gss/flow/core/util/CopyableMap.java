package cn.gss.flow.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JackieGao on 2020/7/13.
 */
public class CopyableMap<K, V> extends HashMap<K, V> implements Copyable<CopyableMap<K, V>> {
  public CopyableMap(final Map<? extends K, ? extends V> m) {
    super(m);
  }

  public CopyableMap() {
  }

  @Override
  public CopyableMap<K, V> copy() {
    return new CopyableMap<>(this);
  }
}
