package cn.gss.flow.core.actor.dispatch;

import akka.actor.ActorContext;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import cn.gss.flow.core.ActorStage;
import cn.gss.flow.core.FlowContext;
import cn.gss.flow.core.actor.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/30.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ActorMessageSender {
  private final ActorSystem actorSystem;
  private final ActorService actorService;

  public void send(
      final ActorContext actorContext,
      final int actorId,
      final List<FlowContext> flowContexts
  ) {
    send(actorContext.parent(), new ActorStage(actorService.read(actorId)), flowContexts);
  }

  public void send(
      final ActorContext actorContext,
      final ActorStage actorStage,
      final List<FlowContext> flowContexts
  ) {
    flowContexts.forEach(context -> context.addStage(actorStage));
    sendInternal(actorContext.parent(), actorStage.getActor().getType().getActorPath(), flowContexts);
  }

  public void send(
      final ActorRef actorRef,
      final int actorId,
      final List<FlowContext> flowContexts
  ) {
    send(actorRef, new ActorStage(actorService.read(actorId)), flowContexts);
  }

  public void send(
      final ActorRef actorRef,
      final ActorStage actorStage,
      final List<FlowContext> flowContexts
  ) {
    flowContexts.forEach(context -> context.addStage(actorStage));
    sendInternal(actorRef, actorStage.getActor().getType().getActorPath(), flowContexts);
  }

  private void sendInternal(
      final ActorRef actorRef,
      final String path,
      final List<FlowContext> flowContexts
  ) {
    flowContexts.forEach(flowContext -> {
      actorSystem.actorSelection(path).tell(flowContext, actorRef);
      if (log.isTraceEnabled()) {
        log.trace(
            "[ActorDispatcher]From {} to {}",
            Optional.ofNullable(actorRef)
                .map(ActorRef::path)
                .map(ActorPath::toStringWithoutAddress)
                .orElse(null),
            path
        );
      }
    });
  }
}
