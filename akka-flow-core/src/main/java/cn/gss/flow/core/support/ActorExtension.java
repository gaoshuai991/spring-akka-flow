package cn.gss.flow.core.support;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

/**
 * Created by JackieGao on 2020/7/13.
 */
@UtilityClass
public class ActorExtension {
  private static ApplicationContext applicationContext;
  private static ActorSystem actorSystem;

  public static void initialize(ApplicationContext context, ActorSystem system) {
    applicationContext = context;
    actorSystem = system;
  }

  public static Props createProps(Class<? extends AbstractActor> actorType, Object... args) {
    return Props.create(ActorProducer.class, applicationContext, actorType, args);
  }

  public static Props createProps(String actorBeanName, Object... args) {
    return Props.create(ActorProducer.class, applicationContext, actorBeanName, args);
  }

  /**
   * Create an actor which annotated with @ActorBean.
   * @param clazz actor class
   * @param args constructor parameters
   * @return actorRef
   */
  public static ActorRef createActor(Class<? extends AbstractActor> clazz, Object... args) {
    return createActor(clazz, clazz.getAnnotation(ActorBean.class).actorName(), args);
  }

  /**
   * Create an actor which annotated with @ActorBean using specified actorName.
   * @param clazz actor class
   * @param actorName actor name(ActorSystem)
   * @param args constructor parameters
   * @return actorRef
   */
  public static ActorRef createActor(Class<? extends AbstractActor> clazz, String actorName, Object... args) {
    return actorSystem.actorOf(createProps(clazz, args), actorName);
  }

  /**
   * Create an actor which already has a BeanDefinition in Spring ApplicationContext.
   * @param actorBeanName actor bean name(Spring).
   * @param actorName actor name(ActorSystem)
   * @param args constructor parameters
   * @return actorRef
   */
  public static ActorRef createActor(String actorBeanName, String actorName, Object... args) {
    return actorSystem.actorOf(createProps(actorBeanName, args), actorName);
  }

  public static String versionize(String actorPathOrName, String version) {
    return actorPathOrName.concat("-").concat(version);
  }

  public static String defaultPath(String actorName) {
    return "/user/".concat(actorName);
  }
}
