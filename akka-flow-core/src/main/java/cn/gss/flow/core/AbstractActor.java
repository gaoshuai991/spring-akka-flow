package cn.gss.flow.core;

import cn.gss.flow.core.actor.dispatch.ActorDispatcher;
import cn.gss.flow.core.actor.dispatch.ActorMessageSender;
import cn.gss.flow.core.util.Copyable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/13.
 */
@Slf4j
public abstract class AbstractActor<C extends FlowContext<C, D>, D extends Copyable<D>> extends BaseActor {
  protected ActorStage stage;
  protected ActorMessageSender actorMessageSender;
  protected ActorDispatcher actorDispatcher;

  @Override
  @SuppressWarnings("unchecked")
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            FlowContext.class,
            flowContext -> {
              try {
                preprocess((C) flowContext);
                final ProcessResult<D> processResult = process0((C) flowContext);
                postprocess((C) flowContext, processResult);
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
            }
        ).build();
  }

  protected void preprocess(C flowContext) {
    stage = flowContext.getCurrentStage();

    MDC.clear();
    Optional.ofNullable(flowContext.getMdc()).ifPresent(MDC::setContextMap);
  }

  protected abstract ProcessResult<D> process0(C flowContext);

  protected void postprocess(C flowContext, ProcessResult<D> processResult) {
    actorDispatcher.dispatch(stage, flowContext, processResult);
  }

  @Autowired
  public void setActorMessageSender(final ActorMessageSender actorMessageSender) {
    this.actorMessageSender = actorMessageSender;
  }

  @Autowired
  public void setActorDispatcher(ActorDispatcher actorDispatcher) {
    this.actorDispatcher = actorDispatcher;
  }
}
