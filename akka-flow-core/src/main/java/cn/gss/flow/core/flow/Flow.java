package cn.gss.flow.core.flow;

import lombok.Data;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
public class Flow {
  private Integer id;
  private String name;
  /**
   * 实际为actor的id，代表整个flow的入口
   */
  private Integer entrance;
}
