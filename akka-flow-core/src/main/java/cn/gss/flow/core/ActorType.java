package cn.gss.flow.core;

/**
 * Created by JackieGao on 2020/7/28.
 */
public interface ActorType {
  static ActorType valueOf(String actorType) {
    return () -> actorType;
  }

  String name();

  default String getActorPath() {
    return "/user/" + toString();
  }
}
