package cn.gss.flow.core.actor.service;

import cn.gss.flow.core.actor.Actor;

/**
 * Created by JackieGao on 2020/7/28.
 */
public interface ActorService {
  Actor read(int id);

  /**
   * @return Generated key
   */
  int create(Actor actor);

  void update(int id, Actor actor);

  void delete(int id);
}
