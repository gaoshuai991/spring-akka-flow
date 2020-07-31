package cn.gss.flow.core.actor.dispatch;

import static org.springframework.util.CollectionUtils.isEmpty;

import akka.actor.ActorRef;
import cn.gss.flow.core.ActorStage;
import cn.gss.flow.core.FlowContext;
import cn.gss.flow.core.ProcessResult;
import cn.gss.flow.core.flow.Flow;
import cn.gss.flow.core.flow.service.FlowService;
import cn.gss.flow.core.util.Copyable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Component
@RequiredArgsConstructor
public class ActorDispatcher<C extends FlowContext<C, D>, D extends Copyable<D>> {
  private final FlowService flowService;
  private final ActorMessageSender actorMessageSender;

  public void dispatch(
      ActorStage stage, C flowContext, ProcessResult<D> processResult
  ) {
    final Map<Integer, List<C>> forwardingMap = new HashMap<>();
    if (stage.getActor().getDispatchMap() != null) {
      for (final Map.Entry<DispatchType, List<DispatchTarget>> entry : stage.getActor().getDispatchMap().entrySet()) {
        final DispatchType forwardDataType = entry.getKey();
        final List<DispatchTarget> dispatchTargets = entry.getValue();
        // transform downstreamIds to actorIds, for nested-flow supporting
        final List<Integer> actorIds = new ArrayList<>();
        for (final DispatchTarget dispatchTarget : dispatchTargets) {
          if (dispatchTarget.getType() == DispatchTarget.TargetType.ACTOR) {
            actorIds.add(dispatchTarget.getId());
          } else {
            Flow flow = flowService.read(dispatchTarget.getId());
            actorIds.add(flow.getEntrance());
          }
        }
        prepareForwarding(forwardDataType, actorIds, forwardingMap, flowContext, processResult);
      }
    }
    if (!isEmpty(forwardingMap)) {
      execForwarding(forwardingMap);
    }
  }



  private void execForwarding(Map<Integer, List<C>> dispatchMap) {
    for (Map.Entry<Integer, List<C>> entry : dispatchMap.entrySet()) {
      Integer actorId = entry.getKey();
      List<C> flowContexts = entry.getValue();
      actorMessageSender.send(ActorRef.noSender(), actorId, (List) flowContexts);
    }
  }

  protected void prepareForwarding(
      DispatchType dispatchType,
      List<Integer> actorIds,
      Map<Integer, List<C>> dispatchMap,
      C flowContext,
      ProcessResult<D> processResult
  ) {
    if (dispatchType == DefaultDispatchType.ALL) {
      actorIds.forEach(actorId -> putForwardingMap(dispatchMap, actorId, flowContext.copy()));
    } else {
      final Map<DispatchType, D> dispatchDataMap = processResult.getDispatchDataMap();
      if (!isEmpty(dispatchDataMap) && dispatchDataMap.get(dispatchType) != null) {
        actorIds.forEach(actorId -> putForwardingMap(
            dispatchMap,
            actorId,
            flowContext.copy().setData(dispatchDataMap.get(dispatchType).copy())
        ));
      }
    }
  }

  protected void putForwardingMap(
      Map<Integer, List<C>> dispatchMap,
      Integer dispatchId,
      C flowContext
  ) {
    dispatchMap.merge(
        dispatchId,
        Collections.singletonList(flowContext),
        (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList())
    );
  }
}
