package cn.gss.flow.core.actor.repo;

import static cn.gss.flow.core.util.ObjectMapperUtils.asString;
import static cn.gss.flow.core.util.ObjectMapperUtils.toValue;

import cn.gss.flow.core.actor.Actor;
import cn.gss.flow.core.actor.dispatch.DispatchTarget;
import cn.gss.flow.core.actor.dispatch.DispatchType;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
public class ActorEntity {
  private int id;
  private String name;
  private String type;
  private String config;
  private String dispatch;

  public static ActorEntity convert(Actor actor) {
    ActorEntity entity = new ActorEntity();
    entity.setId(actor.getId());
    entity.setName(actor.getName());
    entity.setType(actor.getType().name());
    entity.setConfig(actor.getConfig());
    entity.setDispatch(asString(actor.getDispatchMap()));
    return entity;
  }

  public Actor convert() {
    Actor actor = new Actor();
    actor.setId(id);
    actor.setName(name);
    actor.setType(this::getType);
    actor.setConfig(config);
    actor.setDispatchMap(toValue(dispatch, new TypeReference<Map<DispatchType, List<DispatchTarget>>>() {}));
    return actor;
  }
}
