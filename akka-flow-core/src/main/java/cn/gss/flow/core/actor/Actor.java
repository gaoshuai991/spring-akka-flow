package cn.gss.flow.core.actor;

import cn.gss.flow.core.ActorType;
import cn.gss.flow.core.actor.dispatch.DispatchTarget;
import cn.gss.flow.core.actor.dispatch.DispatchType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
public class Actor {
    private int id;
    private String name;
    private ActorType type;
    private String config;
    private Map<DispatchType, List<DispatchTarget>> dispatchMap;
}
