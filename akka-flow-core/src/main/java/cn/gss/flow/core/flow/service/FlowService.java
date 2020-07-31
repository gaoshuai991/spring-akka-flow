package cn.gss.flow.core.flow.service;

import cn.gss.flow.core.flow.Flow;

/**
 * Created by JackieGao on 2020/7/29.
 */
public interface FlowService {
  Flow read(int id);

  /**
   * @return Generated key
   */
  int create(Flow flow);

  void update(int id, Flow flow);

  void delete(int id);
}
