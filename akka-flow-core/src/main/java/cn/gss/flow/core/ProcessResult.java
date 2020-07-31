package cn.gss.flow.core;

import cn.gss.flow.core.actor.dispatch.DispatchType;
import lombok.Data;

import java.util.Map;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
public class ProcessResult<D> {
  private Map<DispatchType, D> dispatchDataMap;
}
