package cn.gss.flow.core.flow.repo;

import cn.gss.flow.core.flow.Flow;
import lombok.Data;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
public class FlowEntity {
  public Integer id;
  private String name;
  /**
   * 实际为actor的id，代表整个flow的入口
   */
  public Integer entrance;

  public static FlowEntity convert(Flow flow) {
    FlowEntity entity = new FlowEntity();
    entity.setId(flow.getId());
    entity.setName(flow.getName());
    entity.setEntrance(flow.getEntrance());
    return entity;
  }

  public Flow convert() {
    Flow flow = new Flow();
    flow.setId(id);
    flow.setName(name);
    flow.setEntrance(entrance);
    return flow;
  }
}
