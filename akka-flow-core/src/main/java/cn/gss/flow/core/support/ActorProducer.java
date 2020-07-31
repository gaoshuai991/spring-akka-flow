package cn.gss.flow.core.support;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * Created by JackieGao on 2020/7/13.
 */
public class ActorProducer implements IndirectActorProducer {
  private ApplicationContext applicationContext;
  private Class<? extends AbstractActor> actorType;
  private String actorBeanName;
  private Object[] args;

  public ActorProducer(
      ApplicationContext applicationContext,
      Class<? extends AbstractActor> actorType,
      Object[] args
  ) {
    this.applicationContext = applicationContext;
    this.actorType = actorType;
    this.args = args;
  }

  public ActorProducer(ApplicationContext applicationContext, String actorBeanName, Object[] args) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
    this.args = args;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<? extends AbstractActor> actorClass() {
    return actorType != null ? actorType
        : (Class<? extends AbstractActor>) applicationContext.getType(actorBeanName);
  }

  @Override
  public Actor produce() {
    if (actorType == null) {
      return (Actor) applicationContext.getBean(actorBeanName, args);
    } else {
      return applicationContext.getBean(actorType, args);
    }
  }
}

