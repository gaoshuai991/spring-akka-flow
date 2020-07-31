package cn.gss.flow.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by JackieGao on 2020/7/13.
 */
public class CopyableList<E> extends ArrayList<E> implements Copyable<CopyableList<E>> {
  public CopyableList() {
  }

  public CopyableList(final Collection<? extends E> c) {
    super(c);
  }

  public static <E> Collector<E, ?, CopyableList<E>> collector() {
    return Collectors.toCollection(CopyableList::new);
  }

  @Override
  public CopyableList<E> copy() {
    return new CopyableList<>(this);
  }
}
