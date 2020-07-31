package cn.gss.flow.core;

import akka.actor.AbstractActor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/10.
 */
@Slf4j
public abstract class BaseActor extends AbstractActor {
  @Override
  public void preRestart(final Throwable reason, final Optional<Object> message) throws Exception {
    log.warn(String.format("%s is restarting due to: %s", self(), message.orElse("Unknown reason")), reason);
    super.preRestart(reason, message);
  }
}
